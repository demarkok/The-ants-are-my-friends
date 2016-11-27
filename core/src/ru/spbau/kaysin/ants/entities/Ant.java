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

import ru.spbau.kaysin.ants.Ants;

public class Ant extends SteeringActor {

    AntWay antWay;
    // Animation
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;

    public Ant(float x, float y) {
        super(false);

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

        float animSpeed = MathUtils.clamp(getLinearVelocity().len() / 100, 0, 100);
        animTime += delta * animSpeed;
        if (getSteeringBehavior() instanceof FollowPath) {
//            antWay.update(((FollowPath<Vector2, LinePath.LinePathParam>) getSteeringBehavior()).getInternalTargetPosition());
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


    //TODO so ugly!
    public void init() {
        getStage().addActor(antWay);
    }
}
