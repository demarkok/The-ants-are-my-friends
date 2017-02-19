package ru.spbau.kaysin.ants.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class GameServer {
    Server server;
    IIdGenerator generator;
    Pool pool;
    Map<Integer, GameConnection> activePlayers;
    Map<Integer, GameSession> activeGames;
    Map<Integer, ObjectSpace> objectSpaceMap;



    public GameServer() throws IOException {
        generator = new IdGenerator();
        pool = new Pool();
        activePlayers = new HashMap<Integer, GameConnection>();
        objectSpaceMap = new HashMap<Integer, ObjectSpace>();
        activeGames = new HashMap<Integer, GameSession>();

        server = new Server() {
            @Override
            protected Connection newConnection () {
                GameConnection connection = new GameConnection();

                return connection;
            }
        };
        Network.register(server);

        server.addListener(new Listener() {

            @Override
            public void received(Connection c, Object object) {
                GameConnection connection = (GameConnection)c;
                if (object instanceof Move) {
                    server.sendToTCP(((GameConnection) c).getEnemyId(), object);
//                    server.sendToAllExceptTCP(c.getID(), object);
                }
            }

            @Override
            public void connected(Connection connection) {
                pool.addPlayer((GameConnection) connection);

                ObjectSpace os = new ObjectSpace(connection);
                os.register(1, generator); // TODO constant variable
                os.register(2, pool);
                objectSpaceMap.put(connection.getID(), os);

            }

            @Override
            public void disconnected(Connection connection) {
                pool.removePlayer((GameConnection) connection);
            }
        });
        server.bind(Network.port);
        server.start();

    }

    static class GameConnection extends Connection {
        private int enemyId = -1;

        public void setEnemyId(int enemyId) {
            this.enemyId = enemyId;
        }

        public int getEnemyId() {
            return enemyId;
        }
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


    public interface IPool {
        public int match(int id);
    }

    public class Pool implements IPool {
        Map <Integer, GameConnection> waitingPlayers;

        public Pool() {
            waitingPlayers = new HashMap<Integer, GameConnection>();
        }

        public void addPlayer(GameConnection connection) {
            waitingPlayers.put(connection.getID(), connection);
        }

        public void removePlayer(GameConnection connection) {
            waitingPlayers.remove(connection.getID());
        }


        @Override
        public int match(int id) {
            GameConnection c = waitingPlayers.get(id);
            if (c == null) {
                if (activePlayers.get(id) != null) {
                    return activePlayers.get(id).getEnemyId();
                } else {
                    return -1;
                }
            }
            if (waitingPlayers.size() < 2) {
                return -1;
            }
            waitingPlayers.remove(id);
            GameConnection enemy = waitingPlayers.values().iterator().next();
            int enemyId = enemy.getID();
            waitingPlayers.remove(enemyId);
            c.setEnemyId(enemyId);
            enemy.setEnemyId(id);
            activePlayers.put(id, c);
            activePlayers.put(enemyId, enemy);

            GameSession game = new GameSession(enemyId, id);
            activeGames.put(id, game);
            activeGames.put(enemyId, game);

            objectSpaceMap.get(id).register(3, game); // TODO constant variable
            objectSpaceMap.get(enemyId).register(3, game);


            return enemyId;
        }
    }

}
