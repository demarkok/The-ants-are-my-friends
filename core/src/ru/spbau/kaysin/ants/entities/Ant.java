package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

import ru.spbau.kaysin.ants.Ants;

public class Ant extends SteeringActor {

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
        setPosition(x, y);
        setOrigin(Align.center);
        setScale(2);

        setBoundingRadius((getHeight() + getWidth()) / 4);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float animSpeed = MathUtils.clamp(getLinearVelocity().len() / 100, 0, 100);
        animTime += delta * animSpeed;
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
        return getPosition().len(x, y) < getBoundingRadius() * getScaleY() * 1.1 ? this : null;
    }
}
