package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;

/**
 * Created by demarkok on 26-Dec-16.
 */
public class LivesMonitor extends Actor {

    private GameWorld world;
    boolean friendly;
    private Sprite texture;

    public LivesMonitor(GameWorld world, boolean friendly) {
        this.friendly = friendly;
        this.world = world;

        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("heart"));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        int lives = friendly ? world.getLives() : world.getEnemyLives();

        float posX = getParent().getWidth() / 2 - texture.getWidth() * lives / 2;
        float posY = friendly ? 0 : getParent().getHeight() - texture.getHeight();

        for (int i = 0; i < lives; ++i) {
            batch.draw(texture, posX, posY);
            posX += texture.getHeight();
        }
    }
}
