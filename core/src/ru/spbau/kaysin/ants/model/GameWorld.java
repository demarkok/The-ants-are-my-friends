package ru.spbau.kaysin.ants.model;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.controls.TouchSourceListener;
import ru.spbau.kaysin.ants.entities.*;
import ru.spbau.kaysin.ants.network.GameClient;
import ru.spbau.kaysin.ants.network.Move;
import ru.spbau.kaysin.ants.screens.MenuScreen;
import ru.spbau.kaysin.ants.utils.ButtonGenerator;
import ru.spbau.kaysin.ants.utils.TextSpawner;

import java.util.*;

import static java.lang.Math.min;
import static ru.spbau.kaysin.ants.utils.GameMathUtils.reflect;

public class GameWorld {

    private ArrayList<HandlingContact> handlingObjects;

    private Group anthills;
    private Group ants;
    private Group hud;
    private Group bonuses;


    private HashMap<Integer, Ant> antMap;
//    private List<Ant> antList;
    private List<Apple> apples;

    private float energy = 0.9f;
    private float energyRecoverySpeed = 0.3f;
    private boolean activeRecovery = false;

    private EnergyBar energyBar;

    private AnthillDomain domain;
    private AnthillCodomain codomain;
    private AnthillDomain enemyDomain;
    private AnthillCodomain enemyCodomain;


    private Stage stage;
    private TweenManager tweenManager;

    private int lives;
    private int enemyLives;

    State state = State.CAPTURE;

    private Move move;

    private GameClient client;

    private float playbackTimer;

    private TextButton doneButton;

    public GameWorld(Stage stage, GameClient client) {


        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        tweenManager = new TweenManager();

        handlingObjects = new ArrayList<HandlingContact>();

        move = new Move();

        this.client = client;
        this.client.init(this);

        lives = 2;
        enemyLives = 2;

        anthills = new Group();
        anthills.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        anthills.setTouchable(Touchable.childrenOnly);
        stage.addActor(anthills);

        addFriendDomain();
        addFriendCodomain();
        addEnemyDomain();
        addEnemyCodomain();

        bonuses = new Group();
        bonuses.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        bonuses.setTouchable(Touchable.disabled);
        stage.addActor(bonuses);


//        antList = new LinkedList<Ant>();
        antMap = new HashMap<Integer, Ant>();
        ants = new Group();
        ants.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        ants.setTouchable(Touchable.childrenOnly);
        stage.addActor(ants);



        hud = new Group();
        hud.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        hud.setTouchable(Touchable.disabled);
        stage.addActor(hud);

        energyBar = new EnergyBar(this);
        hud.addActor(energyBar);
        energyBar.init();


        hud.addActor(new LivesMonitor(this));


        doneButton = ButtonGenerator.generateButton("done", 50, stage.getWidth() / 2, 0);
        doneButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchState();
            }
        });
        stage.addActor(doneButton);

        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new DragTheAntListener(this));
        stage.addListener(new TouchSourceListener(this));

    }

    public void addAnt(float x, float y, int id, boolean friendly) {
        Ant ant = new Ant(x, y, this, friendly);
//        antList.add(ant);
        antMap.put(id, ant);
        ants.addActor(ant);
        ant.init();
        move.addNewAnt(ant, id);
    }


    private void addBonus(Bonus bonus) {
        bonus.initialize(this);
        addHandling(bonus);
        bonuses.addActor(bonus);

    }

    private void addFriendDomain() {
        domain = new AnthillDomain(true);
        anthills.addActor(domain);
        domain.init();
    }

    private void addFriendCodomain() {
        codomain = new AnthillCodomain(true);
        anthills.addActor(codomain);
        codomain.init();
        addHandling(codomain);
    }

    private void addEnemyDomain() {
        enemyDomain = new AnthillDomain(false);
        anthills.addActor(enemyDomain);
        enemyDomain.init();
    }

    private void addEnemyCodomain() {
        enemyCodomain = new AnthillCodomain(false);
        anthills.addActor(enemyCodomain);
        enemyCodomain.init();
        addHandling(enemyCodomain);
    }


    public void draw() {
        stage.draw();
    }

    public void update(float dt) {
        playbackTimer += dt;

        if (state == State.PLAYBACK && playbackTimer > 5) { // check if the action ended
            System.out.println(playbackTimer);
            boolean end = true;
            for (Ant ant: antMap.values()) {
                if (ant.isMoving()) {
                    end = false;
                    break;
                }
            }
            if (end) {
                switchState();
            }
        }

        if (state == State.WAITING) {
            Move eMove = client.getMove();
            if (eMove != null) {
                processMove(eMove);
                switchState();
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switchState();
        }


        stage.act(dt);
        tweenManager.update(dt);
        if (activeRecovery) {
            setEnergy(energy + energyRecoverySpeed * dt);
        }

        processContacts();
        cleanUp();

    }

    public Stage getStage() {
        return stage;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = MathUtils.clamp(energy, 0, 1);
    }

    public float getEnergyRecoverySpeed() {
        return energyRecoverySpeed;
    }

    public void setEnergyRecoverySpeed(float energyRecoverySpeed) {
        this.energyRecoverySpeed = energyRecoverySpeed;
    }

    public TweenManager getTweenManager() {
        return tweenManager;
    }

    public void addHandling(HandlingContact actor) {
        handlingObjects.add(actor);
    }

    public GameClient getClient() {
        return client;
    }

    public State getState() {
        return state;
    }

    public EnergyBar getEnergyBar() {
        return energyBar;
    }


    public void cleanUp() {
        Iterator<Ant> it = antMap.values().iterator();
        while (it.hasNext()) {
            if (!it.next().isAlive()) {
                it.remove();
            }
        }
    }

    private void processContacts() {

        for (Ant ant: antMap.values()) {
            for (HandlingContact o: handlingObjects) {
                if (o.haveContact(ant)) {
                    ant.visitHandlingContact(o);
                    o.processContact(ant);
                }
            }
        }


        int n = antMap.size();
        Ant[] antArray = new Ant[n];
        antMap.values().toArray(antArray);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (antArray[i].haveContact(antArray[j]) && antArray[j].haveContact(antArray[i])) {
                    antArray[i].processContact(antArray[j]);
                    antArray[j].processContact(antArray[i]);
                }
            }
        }
    }

    public void decLives() {
        lives--;
        if (lives == 0) {
            lose();
        }
    }

    public void decEnemyLives() {
        enemyLives--;
        if (enemyLives == 0) {
            win();
        }
    }

    private void lose() {
        TextSpawner.spawnText("defeat!", Ants.WIDTH / 2, Ants.HEIGHT / 2, this, Color.BLACK);
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {

                Ants.getInstance().setScreen(new MenuScreen());
            }
        }, 4);
    }

    private void win() {
        TextSpawner.spawnText("victory!", Ants.WIDTH / 2, Ants.HEIGHT / 2, this, Color.BLACK);
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                Ants.getInstance().setScreen(new MenuScreen());
            }
        }, 4);
    }

    public int getLives() {
        return lives;
    }


    public enum State {
        CAPTURE, WAITING, PLAYBACK
    }

    public void switchState() {
        if (state == State.CAPTURE) { // CAPTURE -> WAITING
            doneButton.setTouchable(Touchable.disabled);

            state = State.WAITING;
            for (Map.Entry<Integer, Ant> entry: antMap.entrySet()) {
                Ant ant = entry.getValue();
                if (ant.isReady()) {
                    move.addAntMovement(entry.getKey(), ant.getAntWay().getPathToFollow());
                }
            }
            client.sendMove(move);
        } else if (state == State.WAITING) { // WAITING -> PLAYBACK
            client.setMove(null);
            state = State.PLAYBACK;
            playbackTimer = 0;
        } else { // PLAYBACK -> CAPTURE
            doneButton.setTouchable(Touchable.enabled);

            state = State.CAPTURE;
            energy = energy + energyRecoverySpeed;
            energy = min(energy, 1);
            for (Ant ant: antMap.values()) {
                ant.clearWay();
            }



            addBonus(client.generateRandomBonus());

            move = new Move();
            System.out.println("PLAYBACK -> CAPTURE");
        }
    }

    public void processMove(Move move) {
        for (Move.NewAnt newAnt : move.getAnts()) {
            Vector2 position = new Vector2(newAnt.x, newAnt.y);
            Vector2 rightPosition = reflect(position);
            addAnt(rightPosition.x, rightPosition.y, newAnt.antId, false);
        }
        for (Move.AntMovement antMovement : move.getMovements()) {
            System.out.println("move:" + antMovement.antId);
            Ant ant = antMap.get(antMovement.antId);
            Array<Vector2> pathToFollow = antMovement.pathToFollow;
            for (Vector2 point : pathToFollow) {
                point.set(reflect(point));
            }
            ant.getAntWay().setPathToFollow(pathToFollow);
            ant.startMovement();
        }
    }

}
