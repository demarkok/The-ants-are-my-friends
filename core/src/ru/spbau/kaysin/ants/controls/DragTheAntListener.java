package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.model.GameWorld;

public class DragTheAntListener extends DragListener {
    private GameWorld world;
    private Ant ant;

    private boolean enabled = false;

    public DragTheAntListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {

        if (world.getState() != GameWorld.State.CAPTURE) {
            return;
        }


        Actor actor = world.getStage().hit(getTouchDownX(), getTouchDownY(), true);
        if (actor instanceof Ant) {
            if (!((Ant) actor).isFriendly()) {
                return;
            }
            init((Ant)actor);
        }
        super.dragStart(event, x, y, pointer);
    }

    private void init(Ant ant) {
        this.ant = ant;
        enabled = true;
        // stop the ant if he had any movements
        ant.setSteeringBehavior(null);

        ant.getAntWay().reset();
//        world.setActiveRecovery(false);
        world.setEnergy(world.getEnergy() - Ant.START_MOVEMENT_FINE);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }
        if (world.getEnergy() <= 0) {
            ant.getWorld().getEnergyBar().shake();
            return;
        }
        Vector2 newPoint = new Vector2(x, y);
        ant.getAntWay().pushPoint(newPoint);

    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }

        ant.startMovement();

//        world.setActiveRecovery(true);
        enabled = false;
    }
}
