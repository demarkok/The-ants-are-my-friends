package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.util.ArrayList;

public class Network {
    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        kryo.register(GameServer.IIdGenerator.class);
        kryo.register(GameServer.IPool.class);
        kryo.register(IGameSession.class);
        kryo.register(Move.class);
        kryo.register(Move.AntMovement.class);
        kryo.register(Move.NewAnt.class);
        kryo.register(Array.class);
        kryo.register(ArrayList.class);
        kryo.register(Object[].class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(com.badlogic.gdx.utils.Array.ArrayIterable.class);
    }

}

