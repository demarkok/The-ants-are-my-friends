package ru.spbau.kaysin.ants.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.screens.MenuScreen;
import ru.spbau.kaysin.ants.screens.PlayScreen;

import java.io.IOException;

public class GameClient {
    private GameWorld gameWorld;
    private Client client;
    private Move move;
    private int enemyId;

    GameServer.IIdGenerator generator;
    GameServer.IPool pool;
    public GameClient() {

        enemyId = -1;

        client = new Client();
        client.start();
        Network.register(client);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Move) {
//                    gameWorld.getMove().merge((Move)object);
//                    gameWorld.processMove((Move)object);
//                    gameWorld.switchState();
//                    if (gameWorld.getState() == GameWorld.State.WAITING) {
//                        gameWorld.processMove((Move)object);
//                        gameWorld.switchState();
//                    }

                    move = (Move) object;

                }
            }

            @Override
            public void connected(Connection connection) {
                generator = ObjectSpace.getRemoteObject(client,1, GameServer.IIdGenerator.class);
                pool = ObjectSpace.getRemoteObject(client, 2, GameServer.IPool.class);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        });



//        generator = ObjectSpace.getRemoteObject(client,1, GameServer.IIdGenerator.class);
    }

    public void connect(final String host) {
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, host, Network.port);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
//                    Ants.getInstance().setScreen(new MenuScreen());
                }
            }
        }.start();
    }
    public boolean isConnected() {
        return client.isConnected();
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }


    public void sendMove(Move move) {
        client.sendTCP(move);
    }

    public int getNewIndex() {
        return generator.getId();
    }

    public void init(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void tryToMatch() {
        if (pool != null) {
            enemyId = pool.match(client.getID());
        }
    }

    public int getEnemyId() {
        return enemyId;
    }
}
