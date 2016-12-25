package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.model.HandlingContact;

public class Ant extends SteeringActor {

    // Constants
    public static final float START_MOVEMENT_FINE = 0.15f; // decrease the energy when the dragging starts
    public static final float ENERGY_CONSUMPTION = 0.0005f;

    private ArrayList<DeferredBonus> deferredBonuses;
    protected GameWorld world;
    private AntWay antWay;
    // Animation
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;

    private boolean friendly;

    private boolean alive;

    public Ant(float x, float y, GameWorld world, boolean friendly) {
        super(false);

        alive = true;
        deferredBonuses = new ArrayList<DeferredBonus>();

        this.world = world;

        this.friendly = friendly;
        String textureName = friendly ? "ant" : "redAnt";

        animation = new Animation(0.08f, Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegions(textureName));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animFrame = animation.getKeyFrame(animTime);

        setSize(animFrame.getRegionWidth(), animFrame.getRegionHeight());
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        setOrigin(Align.center);
        setScale(2);

        setBoundingRadius((getHeight() + getWidth()) / 4);
        setTouchable(Touchable.enabled);

        antWay = new AntWay();
    }


    @Override
    public void act(float delta) {
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


    // TODO should fix it, init seems ugly
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
        remove();

        if (anthillCodomain.isFriendly() == friendly) {
            for (DeferredBonus bonus : deferredBonuses) {
                bonus.activate(isFriendly());
            }
            deferredBonuses.clear();
        } else {
            if (friendly) {
                world.decreaseEnemyLives();
            } else {
                world.decreaseLives();
            }

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
}
