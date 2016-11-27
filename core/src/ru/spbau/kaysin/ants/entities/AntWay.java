package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedList;

import ru.spbau.kaysin.ants.Ants;

public class AntWay extends Actor {
    private static final float distance = 16; // distance between points we draw
    Sprite dot;
    private LinkedList<Vector2> points;

    public AntWay() {
        dot = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("dot"));
        init();
    }

    public void pushPoint(Vector2 point) {
        if (points.isEmpty() || points.getLast().dst(point) >= distance) {
            points.add(point);
        }
    }

    public void init() {
        points = new LinkedList<Vector2>();
    }

    public void update(Vector2 position, float epsilon) {
        while (!points.isEmpty() && points.peek().epsilonEquals(position, epsilon)) {
            points.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Vector2 point: points) {
            batch.draw(dot, point.x, point.y);
        }
    }
}
