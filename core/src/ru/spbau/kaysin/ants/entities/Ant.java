package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.controls.TouchController;
import ru.spbau.kaysin.ants.model.GameWorld;

public class Ant extends Entity {

    // private Arrive<Vector2> behavior;
    private TouchController controller;
    private Vector2 target;
    private Vector2 velocity = new Vector2();
    private static final float SPEED = 500;
    private static final float SCL = 4f;


    // Animation
    private Animation animation;
    private TextureRegion animFrame;
    private float animTime = 0;

    public Ant(float x, float y, TouchController controller) {

        super(x, y, 3, 3);
        this.controller = controller;

        target = new Vector2(x, y);

        animation = new Animation(0.033f, Ants.getAssets().get("ant.txt", TextureAtlas.class).findRegions("ant"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animFrame = animation.getKeyFrame(animTime);
    }


    public void draw(SpriteBatch batch) {
        animFrame = animation.getKeyFrame(animTime);


        batch.draw(animFrame, position.x - animFrame.getRegionWidth() * SCL / 2,
                position.y - animFrame.getRegionHeight() * SCL / 2,
                animFrame.getRegionWidth() * SCL / 2, animFrame.getRegionHeight() * SCL / 2,
                animFrame.getRegionWidth() * SCL, animFrame.getRegionHeight() * SCL, 1, 1, rotation);

    }

    public void update(float dt) {
        if (controller.isValid()) {
            target = controller.getPoint();
        }

        if (Vector2.dst2(position.x, position.y, target.x, target.y) < 15) {
            velocity = new Vector2(0, 0);
        } else {
            Vector2 direction = new Vector2(target);
            direction.sub(position)
                    .nor()
                    .scl(SPEED);
            velocity = direction.scl(dt);
        }

        if (!velocity.isZero()) {
            rotation = -90 + velocity.angle();
        }

        position.add(velocity);
        float animSpeed = MathUtils.clamp(velocity.len(), 0, 1);
        animTime += dt * animSpeed;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleBeginContact(Entity entity, GameWorld world) {

    }
}
