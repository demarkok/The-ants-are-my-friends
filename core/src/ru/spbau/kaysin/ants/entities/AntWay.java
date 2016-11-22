package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Vector;


public class AntWay {
    private Vector<Vector2> points;

    public void addPoint(Vector2 point) {
        points.add(point);
    }

    private ShapeRenderer renderer;

    public AntWay(ShapeRenderer renderer) {
        points = new Vector<Vector2>();
        this.renderer = renderer;
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
