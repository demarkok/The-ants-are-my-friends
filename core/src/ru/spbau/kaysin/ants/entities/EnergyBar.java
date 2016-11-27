package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ru.spbau.kaysin.ants.model.GameWorld;

/**
 * Created by demarkok on 26-Nov-16.
 */

public class EnergyBar extends Actor {

    private GameWorld world;

    private NinePatchDrawable loadingBarBackground;
    private NinePatchDrawable loadingBar;

    public EnergyBar(GameWorld world) {
        this.world = world;
        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("pack.txt"));
        NinePatch loadingBarBackgroundPatch = new NinePatch(skinAtlas.findRegion("default-round"), 0, 0, 0, 0);
        NinePatch loadingBarPatch = new NinePatch(skinAtlas.findRegion("default-round-down"), 0, 0, 0, 0);
        loadingBar = new NinePatchDrawable(loadingBarPatch);
        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
    }

    // we don't know our parent in constructor
    // TODO: fix it, it's ugly
    public void init() {
        setPosition(20, 20);
        setSize(getParent().getWidth() - 2 * getX(), 10);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        loadingBarBackground.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        loadingBar.draw(batch, getX(), getY(), world.getEnergy() * getWidth() * getScaleX(), getHeight() * getScaleY());
    }
}
