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
import ru.spbau.kaysin.ants.network.GameClient;

public class WaitingScreen implements Screen {

    private SpriteBatch batch;
    private Texture bg;

    // FONTS
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font12;
    private State loadingState;

    private OrthographicCamera cam;
    private Viewport viewport;

    GameClient client;

    private static final float POLLING_TIME = 1;
    private float timer = 0;

    public WaitingScreen() {

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

        loadingState  = State.CONNECTING;

        client = new GameClient();
//        client.connect("192.168.56.101");
//        client.connect("138.201.158.54");
        client.connect("localhost");
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        timer += delta;
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bg, 0, 0, Ants.WIDTH, Ants.HEIGHT);
        GlyphLayout layout = new GlyphLayout(font12, loadingState.getText());
        font12.draw(batch, layout, Ants.WIDTH / 2 - layout.width / 2, Ants.HEIGHT * 3 / 4);
        batch.end();

        if (loadingState == State.CONNECTING && client.isConnected()) {
            loadingState = State.MATCHING;
        }
//        else if (loadingState == State.MATCHING && client.isMatched()) {
//            Ants.getInstance().setScreen(new PlayScreen(client));
//        }
        if (loadingState == State.MATCHING) {
            if (client.getEnemyId() != -1) {
                Ants.getInstance().setScreen(new PlayScreen(client));
            } else {
                if (timer >= POLLING_TIME) {
                    timer = 0;
                    client.tryToMatch();
                }
            }
        }

        if (Gdx.input.justTouched()) {
            Ants.getInstance().setScreen(new PlayScreen(client));
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

    private enum State {
        CONNECTING ("connecting..."),
        MATCHING ("matching...");

        private String text;
        State(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
