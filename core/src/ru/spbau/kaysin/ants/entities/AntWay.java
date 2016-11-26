package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class AntWay {
    private LinkedList<Vector2> points;
    private ShapeRenderer renderer;

    public void pushPoint(Vector2 point) {
        points.add(point);
    }

    public void init() {
        points = new LinkedList<Vector2>();
    }

    public AntWay() {
        this.renderer = new ShapeRenderer();
        init();
    }

    public void update(Vector2 position, float epsilon) {

        while (!points.isEmpty() && points.peek().epsilonEquals(position, epsilon)) {
            points.remove();
        }
    }

    public void draw() {
        renderer.begin(ShapeRenderer.ShapeType.Point);
        for (Vector2 point: points) {
            renderer.point(point.x, point.y, 0);
        }
        renderer.setColor(Color.BLACK);
        renderer.end();
    }
}
