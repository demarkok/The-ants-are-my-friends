package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ru.spbau.kaysin.ants.model.GameWorld;

public class EnergyBar extends Actor {

    private GameWorld world;
    private NinePatchDrawable loadingBarBackground;
    private NinePatchDrawable loadingBar;

    private static final float SHAKE_TIME = 0.1f;
    private float defaultX; // to shake the bar varying super.x
    private float shakeTimer = 0;


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
        setPosition(20, 200);
        setSize(7, getParent().getHeight() - 2 * getY());
        defaultX = getX();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        loadingBarBackground.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        loadingBar.draw(batch, getX(), getY(), getWidth() * getScaleX(), world.getEnergy() * getHeight() * getScaleY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (shakeTimer < 0) {
            shakeTimer = 0;
        }

        if (shakeTimer > 0) {
            shakeTimer -= delta;
            setX(defaultX + MathUtils.random(-1, 1));
        }
    }

    public void shake() {
        shakeTimer = SHAKE_TIME;
    }
}
