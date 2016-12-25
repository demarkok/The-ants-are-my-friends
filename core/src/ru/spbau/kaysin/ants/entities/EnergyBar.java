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
    private static final float SHAKE_DEVIATION = 3;
    private float defaultX; // to shake the bar varying super.x
    private float shakeTimer = 0;

    boolean friendly;

    public EnergyBar(GameWorld world, boolean friendly) {
        this.friendly = friendly;
        this.world = world;
        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("pack.txt"));
        NinePatch loadingBarBackgroundPatch = new NinePatch(skinAtlas.findRegion("default-round"), 0, 0, 0, 0);
        NinePatch loadingBarPatch = new NinePatch(skinAtlas.findRegion("default-round-down"), 0, 0, 0, 0);
        loadingBar = new NinePatchDrawable(loadingBarPatch);
        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
    }

    // we don't know our parent in constructor
    // TODO: get rid of init
    public void init() {
        if (friendly) {
            setPosition(20, 200);
            setSize(7, getParent().getHeight() - 2 * getY());
        } else {
            setPosition(getParent().getWidth() - 20, getParent().getHeight() - 200);
            setSize(7, getParent().getHeight() - 2 * getY());
        }
        defaultX = getX();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float energy;
        if (friendly) {
            energy = world.getEnergy();
        } else {
            energy = world.getEnemyEnergy();
        }
        loadingBarBackground.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        loadingBar.draw(batch, getX(), getY(), getWidth() * getScaleX(), energy * getHeight() * getScaleY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (shakeTimer < 0) {
            shakeTimer = 0;
        }

        if (shakeTimer > 0) {
            shakeTimer -= delta;
            setX(defaultX + MathUtils.random(-SHAKE_DEVIATION, SHAKE_DEVIATION));
        }
    }

    public void shake() {
        shakeTimer = SHAKE_TIME;
    }
}
