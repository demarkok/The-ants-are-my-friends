package ru.spbau.kaysin.ants.utils;

import com.badlogic.gdx.math.Vector2;
import ru.spbau.kaysin.ants.Ants;

public final class GameMathUtils {

    private GameMathUtils() {
    }

    public static float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public static Vector2 reflect(Vector2 vector) {
        return new Vector2(Ants.WIDTH - vector.x, Ants.HEIGHT - vector.y);
    }
}
