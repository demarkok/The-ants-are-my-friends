package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.model.HandlingContact;

public class Ant extends SteeringActor implements HandlingContact {

    // Constants
    public static final float START_MOVEMENT_FINE = 0.2f; // decrease the energy when the dragging starts
    public static final float ENERGY_CONSUMPTION = 0.001f;

    private GameWorld world;
    private AntWay antWay;
    // Animation
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;

    public Ant(float x, float y, GameWorld world) {
        super(false);
        this.world = world;

        animation = new Animation(0.08f, Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegions("ant"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animFrame = animation.getKeyFrame(animTime);

        setBounds(x, y, animFrame.getRegionWidth(), animFrame.getRegionHeight());
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
        world.addHandling(this);
    }

    @Override
    public void processContact(HandlingContact actor) {
//        System.out.println("ANT HERE!");
    }

    @Override
    public boolean haveContact(HandlingContact entity) {
        if (entity instanceof Actor) {
            Actor actor = (Actor)entity;
            Vector2 centerFirst = new Vector2(getX(Align.center), getY(Align.center));
            Vector2 centerSecond = new Vector2(actor.getX(Align.center), actor.getY(Align.center));
            float distance = centerFirst.dst(centerSecond);
            return distance <= getBoundingRadius();
        }
        else {
            return false;
        }
    }
}
