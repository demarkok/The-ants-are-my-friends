package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.spbau.kaysin.ants.controls.TouchController;
import ru.spbau.kaysin.ants.entities.Ant;

public class GameWorld {

    private Ant ant;
    private TouchController controller;

    public GameWorld() {
        controller = new TouchController();
        ant = new Ant(300, 110, controller);
        Gdx.input.setInputProcessor(controller);
    }

    public void draw(SpriteBatch batch) {
        ant.draw(batch);
    }

    public void update(float dt) {
        ant.update(dt);
    }
}
