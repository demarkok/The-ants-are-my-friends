package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;

public class Apple extends Actor {

    private Sprite texture;

    public Apple(float x, float y) {
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("apple"));
        setBounds(x, y, texture.getWidth(), texture.getHeight());
        setOrigin(Align.center);
        setScale(1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
