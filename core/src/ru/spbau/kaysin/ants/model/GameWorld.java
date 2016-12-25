package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aurelienribon.tweenengine.TweenManager;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.controls.TouchSourceListener;
import ru.spbau.kaysin.ants.entities.*;
import ru.spbau.kaysin.ants.utils.TextSpawner;

public class GameWorld {

    private ArrayList<HandlingContact> handlingObjects;

    private Group anthills;
    private Group ants;
    private Group hud;
    private Group bonuses;

    private List<Ant> antList;
    private List<Apple> apples;

    private float energy = 0.9f;
    private float energyRecoverySpeed = 0.1f;
    private boolean activeRecovery = true;
    private EnergyBar energyBar;

    private float enemyEnergy = 0.9f;
    private float enemyEnergyRecoverySpeed = 0.1f;
    private boolean enemyActiveRecovery = true;
    private EnergyBar enemyEnergyBar;

    private Stage stage;

    private TweenManager tweenManager;

    private long lastBonusAppearingTime;

    private int lives;
    private int enemyLives;

    public GameWorld(Stage stage) {

        lives = 5;
        enemyLives = 5;

        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        tweenManager = new TweenManager();

        handlingObjects = new ArrayList<HandlingContact>();


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


        antList = new LinkedList<Ant>();
        ants = new Group();
        ants.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        ants.setTouchable(Touchable.childrenOnly);
        stage.addActor(ants);



        hud = new Group();
        hud.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        hud.setTouchable(Touchable.disabled);
        stage.addActor(hud);

        energyBar = new EnergyBar(this, true);
        hud.addActor(energyBar);
        energyBar.init();

        enemyEnergyBar = new EnergyBar(this, false);
        hud.addActor(enemyEnergyBar);
        enemyEnergyBar.init();


        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new DragTheAntListener(this));
        stage.addListener(new TouchSourceListener(this));


        lastBonusAppearingTime = System.currentTimeMillis();


    }

    public void addAnt(float x, float y, boolean friendly) {
        Ant ant = new Ant(x, y, this, friendly);
        antList.add(ant);
        ants.addActor(ant);
        ant.init();
    }


    private void addApple(float x, float y) {
        Apple apple = new Apple(x, y, this);
        bonuses.addActor(apple);
        addHandling(apple);
    }

    private void addBlueberry(float x, float y) {
        Blueberry blueberry = new Blueberry(x, y, this);
        bonuses.addActor(blueberry);
        addHandling(blueberry);
    }

    private void addFriendDomain() {
        AnthillDomain domain = new AnthillDomain(true);
        anthills.addActor(domain);
        domain.init();
    }

    private void addFriendCodomain() {
        AnthillCodomain codomain = new AnthillCodomain(true);
        anthills.addActor(codomain);
        codomain.init();
        addHandling(codomain);
    }

    private void addEnemyDomain() {
        AnthillDomain domain = new AnthillDomain(false);
        anthills.addActor(domain);
        domain.init();
    }

    private void addEnemyCodomain() {
        AnthillCodomain codomain = new AnthillCodomain(false);
        anthills.addActor(codomain);
        codomain.init();
        addHandling(codomain);
    }


    public void draw() {
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
        tweenManager.update(dt);

        if (activeRecovery) {
            setEnergy(energy + energyRecoverySpeed * dt);
        }
        if (energy == 0) {
            energyBar.shake();
        }

        if (enemyActiveRecovery) {
            setEnemyEnergy(enemyEnergy + enemyEnergyRecoverySpeed * dt);
        }
        if (enemyEnergy == 0) {
            enemyEnergyBar.shake();
        }

        processContacts();
        cleanUp();

        // Just for demonstration
        if (System.currentTimeMillis() - lastBonusAppearingTime > 10000) { // 10 seconds
            lastBonusAppearingTime = System.currentTimeMillis();
            Vector2 pos = new Vector2(MathUtils.random(bonuses.getWidth()), MathUtils.random(bonuses.getHeight()));
            if (MathUtils.randomBoolean()) {
                addApple(pos.x, pos.y);
            } else {
                addBlueberry(pos.x, pos.y);
            }
        }
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

    public void setActiveRecovery(boolean activeRecovery) {
        this.activeRecovery = activeRecovery;
    }

    public void addHandling(HandlingContact actor) {
        handlingObjects.add(actor);
    }

    public void cleanUp() {
        Iterator<Ant> it = antList.iterator();
        while (it.hasNext()) {
            if (!it.next().isAlive()) {
                it.remove();
            }
        }
    }

    private void processContacts() {

        for (Ant ant: antList) {
            for (HandlingContact o: handlingObjects) {
                if (o.haveContact(ant)) {
                    ant.visitHandlingContact(o);
                    o.processContact(ant);
                }
            }
        }


        int n = antList.size();
        Ant[] antArray = new Ant[n];
        antList.toArray(antArray);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (antArray[i].haveContact(antArray[j]) && antArray[j].haveContact(antArray[i])) {
                    antArray[i].processContact(antArray[j]);
                    antArray[j].processContact(antArray[i]);
                }
            }
        }
    }

    public TweenManager getTweenManager() {
        return tweenManager;
    }

    public void setEnemyEnergy(float enemyEnergy) {
        this.enemyEnergy = MathUtils.clamp(enemyEnergy, 0, 1);
    }

    public float getEnemyEnergy() {
        return enemyEnergy;
    }

    public void setEnemyActiveRecovery(boolean enemyActiveRecovery) {
        this.enemyActiveRecovery = enemyActiveRecovery;
    }

    public void setEnemyEnergyRecoverySpeed(float enemyEnergyRecoverySpeed) {
        this.enemyEnergyRecoverySpeed = enemyEnergyRecoverySpeed;
    }

    public float getEnemyEnergyRecoverySpeed() {
        return enemyEnergyRecoverySpeed;
    }

    public void decreaseLives() {
        lives--;
        if (lives == 0) {
            TextSpawner.spawnText("Red win!", stage.getWidth() / 2, stage.getHeight() / 2, this, Color.RED);
        }
    }

    public void decreaseEnemyLives() {
        enemyLives--;
        if (enemyLives == 0) {
            TextSpawner.spawnText("Brown win!", stage.getWidth() / 2, stage.getHeight() / 2, this, Color.BROWN);
        }
    }

}
