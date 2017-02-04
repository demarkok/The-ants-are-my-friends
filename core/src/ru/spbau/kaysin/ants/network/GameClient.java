package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import ru.spbau.kaysin.ants.model.GameWorld;

import java.io.IOException;

/**
 * Created by demarkok on 25-Dec-16.
 */
public class GameClient {
    private GameWorld gameWorld;
    Client client;

    Move move;

    private boolean valid;
    int index;
    GameServer.IIdGenerator generator;
    public GameClient() {
        index = 1;
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
        });

        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, "localhost", Network.port);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();

        generator = ObjectSpace.getRemoteObject(client,1, GameServer.IIdGenerator.class);
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
}
