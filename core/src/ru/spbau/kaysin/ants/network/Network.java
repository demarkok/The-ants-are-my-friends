package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

/**
 * Created by demarkok on 25-Dec-16.
 */
public class Network {
    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Move.class);
        kryo.register(Move.AntMovement.class);
        kryo.register(Move.NewAnt.class);
        kryo.register(Array.class);
        kryo.register(GetNewIndex.class);
        kryo.register(NewIndex.class);
        kryo.register(ArrayList.class);
        kryo.register(Object[].class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
    }

    static public class GetNewIndex {
    }

    static public class NewIndex {
        int index;
        NewIndex() {
        }
        NewIndex(int index) {
            this.index = index;
        }
    }
}
