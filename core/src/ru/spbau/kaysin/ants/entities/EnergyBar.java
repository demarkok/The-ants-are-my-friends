package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ru.spbau.kaysin.ants.model.GameWorld;

import static java.lang.Math.min;

public class EnergyBar extends Actor {

    private GameWorld world;
    private NinePatchDrawable backgroundBar;
    private NinePatchDrawable recoveryBar;
    private NinePatchDrawable energyBar;

    private static final float SHAKE_TIME = 0.1f;
    private static final float SHAKE_DEVIATION = 3;
    private float defaultX; // to shake the bar varying super.x
    private float shakeTimer = 0;


    public EnergyBar(GameWorld world) {
        this.world = world;
        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("pack.txt"));
        NinePatch backgroundBarPatch = new NinePatch(skinAtlas.findRegion("backgroundBar"), 0, 0, 0, 0);
        NinePatch energyBarPatch = new NinePatch(skinAtlas.findRegion("energyBar"), 0, 0, 0, 0);
        NinePatch recoveryBarPatch = new NinePatch(skinAtlas.findRegion("recoveryBar"), 0, 0, 0, 0);
        energyBar = new NinePatchDrawable(energyBarPatch);
        backgroundBar = new NinePatchDrawable(backgroundBarPatch);
        recoveryBar = new NinePatchDrawable(recoveryBarPatch);
    }

    // we don't know our parent in constructor
    // TODO: get rid of reset
    public void init() {
        setPosition(20, 200);
        setSize(7, getParent().getHeight() - 2 * getY());
        defaultX = getX();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float backgroundHeight = getHeight() * getScaleY();
        backgroundBar.draw(batch, getX(), getY(), getWidth() * getScaleX(), backgroundHeight);
        float energyHeight = world.getEnergy() * backgroundHeight;
        energyBar.draw(batch, getX(), getY(), getWidth() * getScaleX(), energyHeight);
        float recoveryHeight = min(backgroundHeight - energyHeight, backgroundHeight * world.getEnergyRecoverySpeed());
        recoveryBar.draw(batch, getX(), getY() + energyHeight, getWidth() * getScaleX(), recoveryHeight);
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
