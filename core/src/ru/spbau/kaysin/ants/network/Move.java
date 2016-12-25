package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.spbau.kaysin.ants.entities.Ant;

import java.util.ArrayList;

/**
 * Created by demarkok on 25-Dec-16.
 */
public class Move {

    private ArrayList<NewAnt> ants;
    private ArrayList<AntMovement> movements;

    public Move() {
        ants = new ArrayList<NewAnt>();
        movements = new ArrayList<AntMovement>();
    }

    public Move(ArrayList<NewAnt> ants, ArrayList<AntMovement> movements) {
        this.ants = ants;
        this.movements = movements;
    }

    public ArrayList<NewAnt> getAnts() {
        return ants;
    }

    public ArrayList<AntMovement> getMovements() {
        return movements;
    }


    public void addNewAnt(Ant ant, int id) {
        ants.add(new NewAnt(id, ant.getX(), ant.getY()));
    }

    public void addAntMovement(int antId, Array<Vector2> pathToFollow){
        movements.add(new AntMovement(antId, pathToFollow));
    }

    public static class NewAnt {
        public int antId;
        public float x, y;

        public NewAnt() {}

        public NewAnt(int antId, float x, float y) {
            this.antId = antId;
            this.x = x;
            this.y = y;
        }
    }

    public static class AntMovement {
        public int antId;
        public Array<Vector2> pathToFollow;

        public AntMovement() {
            pathToFollow = new Array<Vector2>();
        }

        public AntMovement(int antId, Array<Vector2> pathToFollow) {
            this.antId = antId;
            this.pathToFollow = pathToFollow;
        }
    }


}
