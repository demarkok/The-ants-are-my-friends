package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;

public class LivesMonitor extends Actor {

    private GameWorld world;
    private Sprite texture;

    public LivesMonitor(GameWorld world) {
        this.world = world;

        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("heart"));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        int lives = world.getLives();

        float posX = getParent().getWidth() - texture.getWidth();
        float posY = getParent().getHeight() / 2 - texture.getHeight() * lives / 2;


        for (int i = 0; i < lives; ++i) {
            batch.draw(texture, posX, posY);
            posY += texture.getHeight();
        }
    }
}
