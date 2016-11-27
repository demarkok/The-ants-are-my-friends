package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;

/**
 * Created by demarkok on 27-Nov-16.
 */

public class AnthillSource extends Actor {
    private Sprite texture;

    public AnthillSource() {
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("source"));
        setTouchable(Touchable.enabled);
    }

    public void init() {
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
//        setBounds(100, 100, texture.getWidth(), texture.getHeight());
        setOrigin(Align.center);
        setScale(7);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.draw(texture, getX(), getY());
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }
}
