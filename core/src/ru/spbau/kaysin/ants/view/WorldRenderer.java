package ru.spbau.kaysin.ants.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;

public class WorldRenderer {

    private GameWorld gameWorld;

    private OrthographicCamera gameCam;

    private Viewport gamePort;
    private SpriteBatch batch;

    public WorldRenderer() {
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(true);
        gamePort = new ScreenViewport(gameCam);
        batch = new SpriteBatch();
    }

    public void setWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void dispose() {
        batch.dispose();
    }

    public void render() {
        gameCam.update();
        batch.setProjectionMatrix(gameCam.combined);
        Gdx.gl.glClearColor(176 / 255f, 196 / 248f, 200 / 222f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        gameWorld.draw(batch);

        batch.end();

    }

    public void resize(int width, int height) {
        gamePort.update(width, height, true);
    }
}
