package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Random;

import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AntWay;

public class GameWorld {

    private Stage stage;

    ArrayList<Ant> ants;

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
        }
        stage.addListener(new DragTheAntListener(this));
    }

    public Stage getStage() {
        return stage;
    }

    public void draw() {
        stage.draw();
        for (Ant ant: ants) {
            ant.getAntWay().draw();
        }
    }

    public void update(float dt) {
        stage.act(dt);
    }
}
