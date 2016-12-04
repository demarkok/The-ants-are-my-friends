package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.controls.DragTheAntListener;
import ru.spbau.kaysin.ants.controls.TouchSourceListener;
import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AnthillDomain;
import ru.spbau.kaysin.ants.entities.Apple;
import ru.spbau.kaysin.ants.entities.EnergyBar;

public class GameWorld {

    private ArrayList<HandlingContact> handlingObjects;

    private Group ants;
    private Group hud;
    private Group bonuses;

    private ArrayList<Apple> apples;

    private float energy = 0.9f;
    private float energyRecoverySpeed = 0.1f;
    private boolean activeRecovery = true;
    private EnergyBar energyBar;

    // FONTS
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font12;

    private AnthillDomain source;

    private Stage stage;

    private long lastAppleAppearingTime = 0;

    public GameWorld(Stage stage) {
        this.stage = stage;
        stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());

        // FONTS
        generator = Ants.getGenerator();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 21;
        parameter.color = Color.BLACK;
        font12 = generator.generateFont(parameter);
//        generator.dispose();


        source = new AnthillDomain(font12);
        stage.addActor(source);
        source.init();

        bonuses = new Group();
        bonuses.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        bonuses.setTouchable(Touchable.disabled);
        stage.addActor(bonuses);


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

        handlingObjects = new ArrayList<HandlingContact>();
    }

    public void addAnt(float x, float y) {
        Ant ant = new Ant(x, y, this);
        ants.addActor(ant);
        ant.init();
    }

    public void addApple(float x, float y) {
        Apple apple = new Apple(x, y, this);
        bonuses.addActor(apple);
        apple.init();
    }

    public void draw() {
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
        if (activeRecovery) {
            setEnergy(energy + energyRecoverySpeed * dt);
        }

        if (energy == 0) {
            energyBar.shake();
        }
        processContacts();

        // Just for demonstration
        if (System.currentTimeMillis() - lastAppleAppearingTime > 10000) { // 10 seconds
//            System.out.println("FOO");
            lastAppleAppearingTime = System.currentTimeMillis();
            addApple(MathUtils.random(bonuses.getWidth()), MathUtils.random(bonuses.getHeight()));
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

    public void setActiveRecovery(boolean activeRecovery) {
        this.activeRecovery = activeRecovery;
    }

    public void addHandling(HandlingContact actor) {
        handlingObjects.add(actor);
    }

    private void processContacts() {
        for (int i = 0; i < handlingObjects.size(); i++) {
            HandlingContact first = handlingObjects.get(i);
            if (first == null) {
                continue;
            }
            for (int j = i + 1; j < handlingObjects.size(); j++) {
                HandlingContact second = handlingObjects.get(j);
                if (second == null) {
                    continue;
                }
                if (first.haveContact(second) && second.haveContact(first)) {
                    first.processContact(second);
                    second.processContact(first);
                }
            }
        }
    }

}
