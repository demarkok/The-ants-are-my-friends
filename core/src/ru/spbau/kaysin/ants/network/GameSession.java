package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.entities.Apple;
import ru.spbau.kaysin.ants.entities.Blueberry;
import ru.spbau.kaysin.ants.entities.Bonus;

import static ru.spbau.kaysin.ants.utils.MyMathUtils.reflect;

public class GameSession implements IGameSession {
    private int first, second;
    private int turn;
    private int subTurn;

    private Vector2 position;
    private int bonusType;

    public GameSession(int first, int second) {
        this.first = first;
        this.second = second;
        turn = 0;
        subTurn = 0;
        updateBonus();
    }


    @Override
    public Vector2 generateRandomPosition(int id) {
        if (id == first) {
            return position;
        } else {
            return reflect(position);
        }
    }

    @Override
    public int generateRandomBonusType(int id) {
       return bonusType;
    }

    @Override
    public void endOfTurn(int id) {
        subTurn++;
        if (subTurn == 2) {
            turn++;
            subTurn = 0;
            updateBonus();
        }
    }

    private void updateBonus() {
        position = new Vector2(MathUtils.random(Ants.WIDTH), MathUtils.random(Ants.HEIGHT));
        bonusType = MathUtils.random(0, 1);
    }

}
