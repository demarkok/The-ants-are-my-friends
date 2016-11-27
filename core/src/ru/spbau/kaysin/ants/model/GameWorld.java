package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Random;

import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.EnergyBar;

public class GameWorld {

    ArrayList<Ant> ants;
    private float energy;
    private float energyRecoverySpeed = 0.1f;
    private boolean activeRecovery = true;
    private EnergyBar energyBar;
    private Stage stage;

    public GameWorld(Stage stage) {
        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        ants = new ArrayList<Ant>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Ant ant = new Ant(random.nextInt(Math.round(stage.getWidth())),
                              random.nextInt(Math.round(stage.getHeight())));
            ants.add(ant);
            stage.addActor(ant);
            ant.init();
        }
        stage.addListener(new DragTheAntListener(this));

        energyBar = new EnergyBar(this);
        stage.addActor(energyBar);
        energyBar.init();
    }

    public void draw() {
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
        if (activeRecovery) {
            setEnergy(energy + energyRecoverySpeed * dt);
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

    public boolean isActiveRecovery() {
        return activeRecovery;
    }

    public void setActiveRecovery(boolean activeRecovery) {
        this.activeRecovery = activeRecovery;
    }
}
