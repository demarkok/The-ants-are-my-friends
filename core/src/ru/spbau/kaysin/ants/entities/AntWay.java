package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.G;
import ru.spbau.kaysin.ants.model.GameWorld;

import java.util.LinkedList;

public class AntWay extends Actor {
    private static final float distance = 16; // distance between points we draw
    private Sprite dot;
    private LinkedList<Vector2> points;

    private Array<Vector2> pathToFollow;
    private Ant ant;

    boolean done;
    private GameWorld world;


    public AntWay(Ant ant) {
        this.ant = ant;
        world = ant.getWorld();
        dot = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("dot"));
        done = false;
        reset();
    }

    public void pushPoint(Vector2 point) {


        if (pathToFollow.size > 0) {
            float segmentLen = point.dst(pathToFollow.get(pathToFollow.size - 1));
            world.setEnergy(world.getEnergy() - G.ENERGY_CONSUMPTION * segmentLen);
        }
        pathToFollow.add(point);

        if (points.isEmpty() || points.getLast().dst(point) >= distance) {
            points.add(point);
        }
    }

    public void reset() {

        pathToFollow = new Array<Vector2>();
        points = new LinkedList<Vector2>();

        done = false;
    }

    public void update(Vector2 position, float epsilon) {
        while (!points.isEmpty() && points.peek().epsilonEquals(position, epsilon)) {
            points.remove();
        }
        if (points.isEmpty()) {
            done = true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Vector2 point: points) {
            batch.draw(dot, point.x, point.y);
        }
    }

    public Array<Vector2> getPathToFollow() {
        return pathToFollow;
    }

    public void setPathToFollow(Array<Vector2> pathToFollow) {
        this.pathToFollow = pathToFollow;
    }
}
