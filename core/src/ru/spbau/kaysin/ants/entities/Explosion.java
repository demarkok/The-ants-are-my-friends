package ru.spbau.kaysin.ants.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;

public class Explosion extends Actor {
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;
    private final static float ANIMATION_SPEED = 0.8f;

    public Explosion(float x, float y) {
        animation = new Animation(0.08f, Ants.getAssets().get("explosion.txt", TextureAtlas.class).findRegions("explosion"));
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        animFrame = animation.getKeyFrame(animTime);

        setSize(animFrame.getRegionWidth(), animFrame.getRegionHeight());
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        setOrigin(Align.center);
//        setScale(0.5f);

        Gdx.input.vibrate((int)(animation.getAnimationDuration() * 500));

    }

    @Override
    public void act(float delta) {
        animTime += delta * ANIMATION_SPEED;
        if (animTime > animation.getAnimationDuration()) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animFrame = animation.getKeyFrame(animTime);
        batch.draw(animFrame, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
