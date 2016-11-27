package ru.spbau.kaysin.ants.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.spbau.kaysin.ants.Ants;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private Texture bg;
    private Texture choose;

    private OrthographicCamera cam;
    private Viewport viewport;

    public MenuScreen() {
        cam = new OrthographicCamera(Ants.WIDTH, Ants.HEIGHT);
        viewport = new ExtendViewport(Ants.WIDTH, Ants.HEIGHT, cam);

        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("bg.png"));
        bg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

//        choose = new Texture(Gdx.files.internal("singleMulti.png"));
//        choose.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0.6f, 0.4f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bg, 0, 0, Ants.WIDTH, Ants.HEIGHT);
        batch.end();

        if (Gdx.input.justTouched()) {
            Ants.getInstance().setScreen(new PlayScreen());
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
    }
}
