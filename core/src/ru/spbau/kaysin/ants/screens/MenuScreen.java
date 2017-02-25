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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.utils.ButtonGenerator;

import static ru.spbau.kaysin.ants.G.WIDTH_PADDING;

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
    private Stage stage;

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
        stage = new Stage(viewport);
//        stage.clear();
        Gdx.input.setInputProcessor(stage);



        Table table = new Table();
        table.setBounds(WIDTH_PADDING, Ants.HEIGHT / 2, Ants.WIDTH - WIDTH_PADDING * 2, Ants.HEIGHT / 2);
        table.left();

//        table.debug();





        TextButton playButton = ButtonGenerator.generateButton("play", 50, Ants.WIDTH / 2, Ants.HEIGHT / 2);
        playButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Ants.getInstance().setScreen(new ConnectingScreen());
            }
        });

        TextButton exitButton = ButtonGenerator.generateButton("exit", 50, Ants.WIDTH / 2, Ants.HEIGHT / 2);
        exitButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

//        final float buttonWidth = playButton.getWidth();
//        exitButton.setWidth(buttonWidth);


        table.add(playButton).left().spaceBottom(30);
        table.row();
        table.add(exitButton).left();


        Image bgImage = new Image(bg);
        bgImage.setBounds(0, 0, Ants.WIDTH, Ants.HEIGHT);
        stage.addActor(bgImage);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        batch.begin();
        stage.draw();
//        batch.draw(bg, 0, 0, Ants.WIDTH, Ants.HEIGHT);
        batch.end();
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
