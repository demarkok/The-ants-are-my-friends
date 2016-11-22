package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.model.GameWorld;

/**
 * Created by demarkok on 25-Nov-16.
 */

public class ClickTheAntListener extends ClickListener {
    private GameWorld world;

    public ClickTheAntListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Actor actor = world.getStage().hit(x, y, true);
        if (actor instanceof Ant) {
            System.out.println(":)");
        } else {
            System.out.println(":(");
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
