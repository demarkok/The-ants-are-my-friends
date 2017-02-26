package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.model.HandlingContact;

import java.util.ArrayList;

public class Ant extends SteeringActor {

    private ArrayList<DeferredBonus> deferredBonuses;


    private GameWorld world;
    private AntWay antWay;
    // Animation
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;

    private boolean friendly;

    private boolean alive;

    private boolean ready;

    private float stopTime = 0;

    public Ant(float x, float y, GameWorld world, boolean friendly) {
        super(false);

        alive = true;
        deferredBonuses = new ArrayList<DeferredBonus>();

        this.world = world;

        this.friendly = friendly;

        configureAnimation();

        configureGeomParamters(x, y);

        setTouchable(Touchable.enabled);

        antWay = new AntWay(this);
    }

    @Override
    public void act(float delta) {

        if (getLinearVelocity().len2() <= 0.01) {
            stopTime += delta;
        } else {
            stopTime = 0;
        }

        if (world.getState() != GameWorld.State.PLAYBACK) {
            return;
        }

        super.act(delta);

        // update animTime to choose correct animFrame
        float animSpeed = MathUtils.clamp(getLinearVelocity().len() / 100, 0, 100);
        animTime += delta * animSpeed;
        if (getSteeringBehavior() instanceof FollowPath) {

            // erase near points from the antWay
            antWay.update(getPosition(), getBoundingRadius());
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        animFrame = animation.getKeyFrame(animTime);
        batch.draw(animFrame, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() != Touchable.enabled) return null;
        return Vector2.len(x, y) < getBoundingRadius() * getScaleY() * 1.1 ? this : null;
    }

    public AntWay getAntWay() {
        return antWay;
    }

    public GameWorld getWorld() {
        return world;
    }

    // TODO should fix it, reset seems ugly
    public void init() {
        getStage().addActor(antWay);
    }

    public void processContact(Ant ant) {
        if (isFriendly() != ant.isFriendly()) {
            world.getStage().addActor(new Explosion(this.getX(), this.getY()));
            remove();
        }
    }

    public void processContact(Apple apple) {
        setMaxLinearSpeed(getMaxLinearSpeed() + 20);
    }

    public void processContact(Blueberry blueberry) {
        deferredBonuses.add(blueberry);
    }

    public void processContact(AnthillCodomain anthillCodomain) {

        if (anthillCodomain.isFriendly() && friendly) { // a friend in the friendly anthill
            remove();
            for (DeferredBonus bonus: deferredBonuses) {
                bonus.activate();
            }
            deferredBonuses.clear();
        } else if (anthillCodomain.isFriendly()) { // an enemy in the friendly anthill
            remove();
            world.decLives();
        } else if (friendly) { // a friend in the enemy anthill
            remove();
            world.decEnemyLives();
        } else { // an enemy in the enemy anthill
            remove();
        }

    }

    public void visitHandlingContact(HandlingContact o) {
        o.acceptContact(this);
    }

    public boolean haveContact(Ant ant) {

        Vector2 centerFirst = new Vector2(getX(Align.center), getY(Align.center));
        Vector2 centerSecond = new Vector2(ant.getX(Align.center), ant.getY(Align.center));
        float distance = centerFirst.dst(centerSecond);
        return distance < getBoundingRadius() * getScaleX();
    }

    @Override
    public boolean remove() {
        alive = false;
        antWay.remove();
        return super.remove();
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void clearWay() {
        ready = false;
        antWay.reset();
        setSteeringBehavior(null);
    }

    public void startMovement() {
        if (antWay.getPathToFollow().size >= 2) {
            // TODO FollowPath algorithm has a bug. Should substitute it to my own.
            ready = true;
            setSteeringBehavior(
                    new FollowPath<Vector2, LinePath.LinePathParam>(
                            this,
                            new LinePath<Vector2>(antWay.getPathToFollow(), true), 10)
                            .setArrivalTolerance(0)
                            .setDecelerationRadius(2));
        }
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isMoving() {
        return stopTime < 0.5;
//        return getLinearVelocity().len2() > 0;
//        return  getSteeringBehavior() != null && getSteeringBehavior().isEnabled();
    }

    private void configureAnimation() {
        String textureName = friendly ? "ant" : "redAnt";
        animation = new Animation(0.08f, Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegions(textureName));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animFrame = animation.getKeyFrame(animTime);
    }


    private void configureGeomParamters(float x, float y) {
        setSize(animFrame.getRegionWidth(), animFrame.getRegionHeight());
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        setOrigin(Align.center);
        setScale(2);

        setBoundingRadius((getHeight() + getWidth()) / 4);
    }
}
