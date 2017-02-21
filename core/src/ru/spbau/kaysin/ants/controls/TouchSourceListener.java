package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.spbau.kaysin.ants.G;
import ru.spbau.kaysin.ants.entities.AnthillDomain;
import ru.spbau.kaysin.ants.model.GameWorld;


public class TouchSourceListener extends ClickListener {
    private GameWorld world;

    public TouchSourceListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

        if (world.getState() != GameWorld.State.CAPTURE) {
            return false;
        }

        Actor actor = world.getStage().hit(x, y, true);
        if (actor instanceof AnthillDomain) {
            if (!((AnthillDomain) actor).isFriendly()) {
                return false;
            }
            world.addAnt(x, y, world.getClient().getNewIndex(), true);
            world.setEnergy(world.getEnergy() - 1.5f * G.START_MOVEMENT_FINE);
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}

