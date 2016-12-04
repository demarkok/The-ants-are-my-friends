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

public class Apple extends Actor implements HandlingContact {

    private Sprite texture;
    private GameWorld world;

    public Apple(float x, float y, GameWorld world) {
        this.world = world;
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("apple2"));
        setBounds(x, y, texture.getWidth(), texture.getHeight());
        setOrigin(Align.center);
        setScale(2);
    }

    public void init() {
        world.addHandling(this);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void processContact(HandlingContact actor) {
        remove();
    }

    @Override
    public boolean haveContact(HandlingContact entity) {
        if (entity instanceof Actor) {
            Actor actor = (Actor)entity;
            Vector2 centerFirst = new Vector2(getX(Align.center), getY(Align.center));
            Vector2 centerSecond = new Vector2(actor.getX(Align.center), actor.getY(Align.center));
            float distance = centerFirst.dst(centerSecond);
            return distance <= getWidth() * getScaleX();
        }
        else {
            return false;
        }
    }
}
