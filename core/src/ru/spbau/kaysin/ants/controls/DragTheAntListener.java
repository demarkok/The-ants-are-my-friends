package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.utils.Scene2dLocation;

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
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }
        pathToFollow.add(new Vector2(x, y));
        world.getAntWay().addPoint(new Vector2(x, y));
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }
//        System.out.println(pathToFollow);
        ant.setSteeringBehavior(
                new FollowPath<Vector2, LinePath.LinePathParam>(
                        ant,
                        new LinePath<Vector2>(pathToFollow, true), 10)
                        .setArrivalTolerance(0)
                        .setDecelerationRadius(5));

        enabled = false;
    }
}
