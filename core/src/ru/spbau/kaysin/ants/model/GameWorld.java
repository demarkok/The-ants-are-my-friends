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

    // FONTS
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font12;

    private AnthillDomain domain;
    private AnthillCodomain codomain;

    private Stage stage;

    private TweenManager tweenManager;


    private long lastBonusAppearingTime;

    public GameWorld(Stage stage) {
        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        tweenManager = new TweenManager();

        handlingObjects = new ArrayList<HandlingContact>();

        // FONTS
        generator = Ants.getGenerator();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.BLACK;
        font12 = generator.generateFont(parameter);
//        generator.dispose();


        anthills = new Group();
        anthills.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        anthills.setTouchable(Touchable.childrenOnly);
        stage.addActor(anthills);

        domain = new AnthillDomain(font12, true);
        anthills.addActor(domain);
        domain.init();

        codomain = new AnthillCodomain(font12, true);
        anthills.addActor(codomain);
        codomain.init();
        addHandling(codomain);



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

        energyBar = new EnergyBar(this);
        hud.addActor(energyBar);
        energyBar.init();


        // now the stage handle all the inputs
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new DragTheAntListener(this));
        stage.addListener(new TouchSourceListener(this));


        lastBonusAppearingTime = System.currentTimeMillis();


    }

    public void addAnt(float x, float y) {
        Ant ant = new Ant(x, y, this, true);
        antList.add(ant);
        ants.addActor(ant);
        ant.init();
    }


    public void addApple(float x, float y) {
        Apple apple = new Apple(x, y, this);
        bonuses.addActor(apple);
        addHandling(apple);
    }

    public void addBlueberry(float x, float y) {
        Blueberry blueberry = new Blueberry(x, y, this);
        bonuses.addActor(blueberry);
        addHandling(blueberry);
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

}
