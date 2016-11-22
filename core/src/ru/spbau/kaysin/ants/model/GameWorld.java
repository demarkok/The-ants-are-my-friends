package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Random;

import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AntWay;

public class GameWorld {

    private Stage stage;

    AntWay antWay;

    public GameWorld(Stage stage) {
        this.stage = stage;
        antWay = new AntWay(new ShapeRenderer());
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            stage.addActor(new Ant(random.nextInt(Math.round(stage.getWidth())),
                                   random.nextInt(Math.round(stage.getHeight()))));
        }
        stage.addListener(new DragTheAntListener(this));
    }

    public Stage getStage() {
        return stage;
    }

    public AntWay getAntWay() {
        return antWay;
    }

    public void draw() {
        stage.draw();
        antWay.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }
}
