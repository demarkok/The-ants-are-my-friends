package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ru.spbau.kaysin.ants.model.GameWorld;

import java.io.IOException;

/**
 * Created by demarkok on 25-Dec-16.
 */
public class GameClient {
    Client client;
    Move move;
    private boolean valid;
    int index;

    public GameClient() {
        index = 1;
        valid = false;
        client = new Client();
        client.start();
        Network.register(client);
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Move) {
                    valid = true;
                    move = (Move)object;
                }
                if (object instanceof Network.NewIndex) {
                    index = ((Network.NewIndex) object).index;
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
    }

    public Move getMove() {
        if (!valid) {
            return null;
        }
        else {
            valid = false;
            return move;
        }
    }

    public void sendMove(Move move) {
        client.sendTCP(move);
    }

    public int getNewIndex() {
        int result = client.getID() * index;
        index++;
        return result;
    }
}
