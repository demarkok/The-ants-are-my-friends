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

    private Array<Vector2> pathToFollow;
    private boolean enabled = false;

    public DragTheAntListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {
        Actor actor = world.getStage().hit(getTouchDownX(), getTouchDownY(), true);
        if (actor instanceof Ant) {
            init((Ant)actor);
        }
        super.dragStart(event, x, y, pointer);
    }

    private void init(Ant ant) {
        this.ant = ant;
        enabled = true;
        // stop the ant if he had any movements
        ant.setSteeringBehavior(null);
        pathToFollow = new Array<Vector2>();
        ant.getAntWay().init();
        world.setActiveRecovery(false);
        world.setEnergy(world.getEnergy() - Ant.START_MOVEMENT_FINE);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled || world.getEnergy() <= 0) {
            return;
        }
        Vector2 newPoint = new Vector2(x, y);
        if (pathToFollow.size > 0) {
            float segmentLen = newPoint.dst(pathToFollow.get(pathToFollow.size - 1));
            world.setEnergy(world.getEnergy() - Ant.ENERGY_CONSUMPTION * segmentLen);
        }
        pathToFollow.add(newPoint);
        ant.getAntWay().pushPoint(newPoint);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }

        if (pathToFollow.size >= 2) {
            // TODO FollowPath algorithm have bug. Should substitute it to my own.
            ant.setSteeringBehavior(
                    new FollowPath<Vector2, LinePath.LinePathParam>(
                            ant,
                            new LinePath<Vector2>(pathToFollow, true), 10)
                            .setArrivalTolerance(0)
                            .setDecelerationRadius(2));
        }
        world.setActiveRecovery(true);
        enabled = false;
    }
}
