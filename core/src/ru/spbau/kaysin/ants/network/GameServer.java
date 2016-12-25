package ru.spbau.kaysin.ants.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * Created by demarkok on 25-Dec-16.
 */
public class GameServer {
    int counter;
    Server server;
    public GameServer() throws IOException {
        counter = 0;
        server = new Server() {
            @Override
            protected Connection newConnection () {
                return new GameConnection();
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
                if (object instanceof Network.GetNewIndex) {
                    server.sendToTCP(c.getID(), new Network.NewIndex(++counter));
                }
            }

        });
        server.bind(Network.port);
        server.start();
    }

    static class GameConnection extends Connection {
        public String name;
    }

    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new GameServer();
    }
}
