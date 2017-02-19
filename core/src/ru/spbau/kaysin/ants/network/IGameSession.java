package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.Vector2;
import ru.spbau.kaysin.ants.entities.Bonus;

/**
 * Created by demarkok on 19-Feb-17.
 */
public interface IGameSession {
    Vector2 generateRandomPosition(int id);
    int generateRandomBonusType(int id);
    void endOfTurn(int id);
}
