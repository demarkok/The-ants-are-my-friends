package ru.spbau.kaysin.ants.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.spbau.kaysin.ants.Ants;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private Texture bg;

    // FONTS
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font12;
    private GlyphLayout layout;
    private final String text = "Click to continue";

    private OrthographicCamera cam;
    private Viewport viewport;

    public MenuScreen() {
        //TODO change viewport
        cam = new OrthographicCamera(Ants.WIDTH, Ants.HEIGHT);
        viewport = new ExtendViewport(Ants.WIDTH, Ants.HEIGHT, cam);

        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("bg.png"));
        bg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        generator = Ants.getGenerator();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.WHITE;
        font12 = generator.generateFont(parameter);

        layout = new GlyphLayout(font12, text);

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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bg, 0, 0, Ants.WIDTH, Ants.HEIGHT);
        font12.draw(batch, layout, Ants.WIDTH / 2 - layout.width / 2, Ants.HEIGHT * 3 / 4);
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
