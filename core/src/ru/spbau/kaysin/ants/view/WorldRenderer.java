package ru.spbau.kaysin.ants.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;

public class WorldRenderer {

    private GameWorld gameWorld;
    private Stage stage;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    public WorldRenderer() {
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(Ants.WIDTH, Ants.HEIGHT, gameCam);
        gamePort.apply();
        stage = new Stage(gamePort, new SpriteBatch());
    }


    public void setWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void dispose() {
        stage.dispose();
    }

    public void render() {
        Gdx.gl.glClearColor(176 / 255f, 196 / 248f, 200 / 222f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameWorld.draw();
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public Stage getStage() {
        return stage;
    }

    public void resize(int width, int height) {
        gamePort.update(width, height, true);
    }
}
