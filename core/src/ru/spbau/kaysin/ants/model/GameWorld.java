package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.controls.TouchSourceListener;
import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AnthillSource;
import ru.spbau.kaysin.ants.entities.EnergyBar;

public class GameWorld {

    private ArrayList<Ant> ants;

    private float energy = 0.9f;
    private float energyRecoverySpeed = 0.1f;
    private boolean activeRecovery = true;
    private EnergyBar energyBar;

    // FONTS
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font12;

    private AnthillSource source;

    private Stage stage;

    public GameWorld(Stage stage) {
        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        // FONTS
        generator = new FreeTypeFontGenerator(Gdx.files.internal("FONTS/visitor1.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 21;
        parameter.color = Color.BLACK;
        font12 = generator.generateFont(parameter);
        generator.dispose();


        source = new AnthillSource(font12);
        stage.addActor(source);
        source.init();

        ants = new ArrayList<Ant>();

        energyBar = new EnergyBar(this);
        stage.addActor(energyBar);
        energyBar.init();

        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new DragTheAntListener(this));
        stage.addListener(new TouchSourceListener(this));
    }

    public void addAnt(float x, float y) {
        Ant ant = new Ant(x, y);
        ants.add(ant);
        stage.addActor(ant);
        ant.init();
    }

    public void draw() {
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
        if (activeRecovery) {
            setEnergy(energy + energyRecoverySpeed * dt);
        }

        if (energy == 0) {
            energyBar.shake();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = MathUtils.clamp(energy, 0, 1);
    }

    public void setActiveRecovery(boolean activeRecovery) {
        this.activeRecovery = activeRecovery;
    }

}
