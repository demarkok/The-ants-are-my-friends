package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.entities.AntWay;
import ru.spbau.kaysin.ants.model.GameWorld;

/**
 * Created by demarkok on 25-Nov-16.
 */

public class DragTheAntListener extends DragListener {
    private GameWorld world;
    private Ant ant;
    private boolean enabled = false;

 //    private FollowPath <Vector2, LinePath.LinePathParam> pathToFollow;
    Array<Vector2> pathToFollow;

    public DragTheAntListener(GameWorld world) {
        this.world = world;
    }

//    @Override
//    public void dragStart(InputEvent event, float x, float y, int pointer) {
//        Actor actor = world.getStage().hit(x, y, true);
//        if (actor instanceof Ant) {
//            init((Ant)actor);
//        } else {
//            System.out.println(":(");
//        }
//    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Actor actor = world.getStage().hit(x, y, true);
        if (actor instanceof Ant) {
            init((Ant)actor);
        }
        return super.touchDown(event, x, y, pointer, button);
    }

    private void init(Ant ant) {
        this.ant = ant;
        enabled = true;
        ant.setSteeringBehavior(null);
        pathToFollow = new Array<Vector2>();
        ant.getAntWay().init();
        world.setActiveRecovery(false);
        //TODO make 0.1f Ant's field
        world.setEnergy(world.getEnergy() - 0.1f);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled || world.getEnergy() <= 0) {
            return;
        }
        Vector2 newPoint = new Vector2(x, y);
        if (pathToFollow.size > 0) {
            float segmentLen = newPoint.dst(pathToFollow.get(pathToFollow.size - 1));
            //TODO make 0.0005 Ant's field
            world.setEnergy(world.getEnergy() - 0.0005f * segmentLen);
        }
        pathToFollow.add(newPoint);
        ant.getAntWay().pushPoint(newPoint);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }
//        System.out.println(pathToFollow);
        try {
            ant.setSteeringBehavior(
                    new FollowPath<Vector2, LinePath.LinePathParam>(
                            ant,
                            new LinePath<Vector2>(pathToFollow, true), 10)
                            .setArrivalTolerance(0)
                            .setDecelerationRadius(2));
        } catch (Exception e) {
        }
        enabled = false;
        world.setActiveRecovery(true);
    }
}
