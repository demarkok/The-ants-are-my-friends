package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.model.HandlingContact;


public abstract class Bonus extends Actor implements HandlingContact {

    private Sprite texture;
    private GameWorld world;
    private boolean captured = false;

    public Bonus(float x, float y, GameWorld world, String textureName) {
        this.world = world;
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion(textureName));
        setBounds(x, y, texture.getWidth(), texture.getHeight());
        setOrigin(Align.center);
        setScale(3);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void processContact(Ant ant) {
        captured = true;
        remove();
    }

    @Override
    public boolean haveContact(Ant ant) {

        if (captured) {
            return false;
        }


        Vector2 centerFirst = new Vector2(getX(Align.center), getY(Align.center));
        Vector2 centerSecond = new Vector2(ant.getX(Align.center), ant.getY(Align.center));
        float distance = centerFirst.dst(centerSecond);
        return distance < getContactDistance();
    }

    private float getContactDistance() {
        return getWidth() * getScaleX();
    }

    public GameWorld getWorld() {
        return world;
    }
}
