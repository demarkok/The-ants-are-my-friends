package ru.spbau.kaysin.ants.screens;

import com.badlogic.gdx.Screen;

import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.view.WorldRenderer;

/**
 * Created by demarkok on 06-Nov-16.
 */
public class PlayScreen implements Screen {

    private WorldRenderer renderer;

    private GameWorld gameWorld;

    public PlayScreen() {

        gameWorld = new GameWorld();
        renderer = new WorldRenderer();
        renderer.setWorld(gameWorld);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        gameWorld.update(dt);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
