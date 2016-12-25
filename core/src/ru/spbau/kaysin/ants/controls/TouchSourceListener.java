package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AnthillDomain;
import ru.spbau.kaysin.ants.model.GameWorld;


public class TouchSourceListener extends ClickListener {
    private GameWorld world;

    public TouchSourceListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Actor actor = world.getStage().hit(x, y, true);
        if (actor instanceof AnthillDomain) {
            AnthillDomain domain = (AnthillDomain)actor;
            world.addAnt(x, y, domain.isFriendly());
            world.setEnergy(world.getEnergy() - 1.5f * Ant.START_MOVEMENT_FINE);
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}

