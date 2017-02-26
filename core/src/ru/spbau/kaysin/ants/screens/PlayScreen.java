package ru.spbau.kaysin.ants.screens;

import com.badlogic.gdx.Screen;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.network.GameClient;
import ru.spbau.kaysin.ants.view.WorldRenderer;

public class PlayScreen implements Screen {

    private WorldRenderer renderer;

    // Logic
    private GameWorld gameWorld;

    private float fixedTimeStepAccumulator = 0f;
    private final float MAX_ACCUMULATED_TIME = 1.0f;
    private static final float TIME_STEP = 1 / 60f;


    public PlayScreen(GameClient client) {
        renderer = new WorldRenderer();
        gameWorld = new GameWorld(renderer.getStage(), client);
        renderer.setWorld(gameWorld);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float dt) {
        // Fixed FPS rendering

        fixedTimeStepAccumulator += dt;
        if (fixedTimeStepAccumulator > MAX_ACCUMULATED_TIME) {
            fixedTimeStepAccumulator = MAX_ACCUMULATED_TIME;
        }

        while (fixedTimeStepAccumulator >= TIME_STEP) {
            gameWorld.update(TIME_STEP);
            fixedTimeStepAccumulator -= TIME_STEP;
        }

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
        renderer.dispose();


    }


}
