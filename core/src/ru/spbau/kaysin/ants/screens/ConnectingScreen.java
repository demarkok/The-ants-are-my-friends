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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.G;
import ru.spbau.kaysin.ants.network.GameClient;
import ru.spbau.kaysin.ants.utils.ButtonGenerator;
import ru.spbau.kaysin.ants.utils.FontUtils;

import static ru.spbau.kaysin.ants.G.HEIGHT_PADDING;
import static ru.spbau.kaysin.ants.G.WIDTH_PADDING;


public class ConnectingScreen implements Screen {
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

    private State loadingState = State.WAITING;
    private Label loadingStateLabel;

    GameClient client;

    public ConnectingScreen() {

        cam = new OrthographicCamera(Ants.WIDTH, Ants.HEIGHT);
        viewport = new ExtendViewport(Ants.WIDTH, Ants.HEIGHT, cam);

        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("bg.png"));
        bg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        generator = Ants.getGenerator();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font12 = generator.generateFont(parameter);

        layout = new GlyphLayout(font12, text);

        client = new GameClient();
    }


    @Override
    public void show() {


        stage = new Stage();
//        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("holo/Holo-Dark-hdpi.json"));
//        skin.addRegions(Ants.getAssets().get("holo/Holo-Dark-hdpi.atlas", TextureAtlas.class));
//        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();


        final TextField addressText = new TextField("46.101.228.47", skin);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font12;
        style.fontColor = Color.WHITE;
//        style.cursor = skin.getDrawable("textfield_cursor");
        addressText.setStyle(style);
        addressText.setWidth(300);



        Table table = new Table();
        table.setBounds(WIDTH_PADDING, Ants.HEIGHT / 2, Ants.WIDTH - WIDTH_PADDING * 2, Ants.HEIGHT / 2);
        table.add(addressText).left().width(addressText.getWidth()).spaceBottom(30);
        table.left();
        table.row();


        TextButton connectButton = ButtonGenerator.generateButton("connect", 50, Ants.WIDTH / 2, Ants.HEIGHT / 2);
        connectButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadingState = State.CONNECTING;
                client.connect(addressText.getText(), new GameClient.ConnectionFailureListener() {
                    @Override
                    public void onFailure() {
                        loadingState = State.ERROR;
                        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                            @Override
                            public void run() {
                                Ants.getInstance().setScreen(new MenuScreen());
                            }
                        }, 2);
                    }
                });
            }
        });
        Image bgImage = new Image(bg);
        bgImage.setBounds(0, 0, Ants.WIDTH, Ants.HEIGHT);
        table.add(connectButton).left();
        stage.addActor(bgImage);
        stage.addActor(table);

        loadingStateLabel = new Label(loadingState.getText(), new Label.LabelStyle(font12, Color.WHITE));

        loadingStateLabel.setPosition(WIDTH_PADDING, HEIGHT_PADDING);

        stage.addActor(loadingStateLabel);


    }

    @Override
    public void render(float delta) {
        loadingStateLabel.setText(loadingState.getText());


        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        batch.begin();
        stage.draw();
        batch.end();


        if (loadingState == State.CONNECTING && client.isConnected()) {
            loadingState = State.MATCHING;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (client.getEnemyId() != -1) {
                        Ants.getInstance().setScreen(new PlayScreen(client));
                        cancel();
                    } else {
                        client.tryToMatch();
                    }
                }
            },0,1);
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
        WAITING (""),
        CONNECTING ("connecting..."),
        MATCHING ("matching..."),
        ERROR ("connection error");

        private String text;
        State(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }


}
