package ru.spbau.kaysin.ants.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class GameServer {
    Server server;
    HashMap<Integer, Move> map;
    IIdGenerator generator;

    Set<Connection> pool;

    public GameServer() throws IOException {
        generator = new IdGenerator();

        server = new Server() {
            @Override
            protected Connection newConnection () {
                GameConnection connection = new GameConnection();
                new ObjectSpace(connection).register(1, generator);
                return connection;
            }
        };
        Network.register(server);

        server.addListener(new Listener() {

            @Override
            public void received(Connection c, Object object) {
                GameConnection connection = (GameConnection)c;

                if (object instanceof Move) {
                    server.sendToAllExceptTCP(c.getID(), object);
                }
            }

        });
        server.bind(Network.port);
        server.start();

        new ObjectSpace();
    }

    static class GameConnection extends Connection {
        public String name;
    }

    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new GameServer();
    }

    public interface IIdGenerator {
        int getId();
    }

    public class IdGenerator implements IIdGenerator {
        int id = 0;

        @Override
        synchronized public int getId() {
            return id++;
        }

    }
}
