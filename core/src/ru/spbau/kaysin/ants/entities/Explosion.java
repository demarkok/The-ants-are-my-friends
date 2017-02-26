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
    private static final float VIBRATION_DURATION = 500;


    public Explosion(float x, float y) {

        configureAnimation();

        configureGeomParameters(x, y);

        Gdx.input.vibrate((int)(animation.getAnimationDuration() * VIBRATION_DURATION));

    }

    private void configureGeomParameters(float x, float y) {
        setSize(animFrame.getRegionWidth(), animFrame.getRegionHeight());
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        setOrigin(Align.center);
    }

    private void configureAnimation() {
        animation = new Animation(0.07f, Ants.getAssets().get("explosion.txt", TextureAtlas.class).findRegions("explosion"));
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        animFrame = animation.getKeyFrame(animTime);
    }



    @Override
    public void act(float delta) {
        animTime += delta;
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
