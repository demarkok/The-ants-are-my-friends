package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;


public class TouchController extends InputAdapter {

    private Vector2 point;
    private boolean valid;

    public TouchController() {
        valid = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("(%d, %d)\n", screenX, screenY);
        point = new Vector2(screenX, screenY);
        valid = true;
        return true;
    }

    public boolean isValid() {
        return valid;
    }

    public Vector2 getPoint() {
        valid = false;
        return point;
    }
}
