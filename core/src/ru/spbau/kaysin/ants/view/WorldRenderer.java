package ru.spbau.kaysin.ants.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;

import static ru.spbau.kaysin.ants.Constants.CAPTURE_COLOR;
import static ru.spbau.kaysin.ants.Constants.PLAYBACK_COLOR;

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
        Color bgColor;

        if (gameWorld.getState() == GameWorld.State.CAPTURE) {
            bgColor = CAPTURE_COLOR;
        }
        else {
            bgColor = PLAYBACK_COLOR;
        }
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
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
