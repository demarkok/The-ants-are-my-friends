package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.Vector2;

public interface IGameSession {
    Vector2 generateRandomPosition(int id);
    int generateRandomBonusType(int id);
    void endOfTurn(int id);
}
