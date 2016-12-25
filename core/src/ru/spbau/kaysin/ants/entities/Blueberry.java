package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.Color;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.utils.TextSpawner;


public class Blueberry extends DeferredBonus {
    public Blueberry(float x, float y, GameWorld world) {
        super(x, y, world, "blueberry");
    }
    @Override
    public void acceptContact(Ant ant) {
        ant.processContact(this);
    }

    @Override
    public void activate(boolean friendly) {
        if (friendly) {
            float currentRecoverySpeed = getWorld().getEnergyRecoverySpeed();
            getWorld().setEnergyRecoverySpeed(currentRecoverySpeed + 0.05f);
        } else {
            float currentRecoverySpeed = getWorld().getEnemyEnergyRecoverySpeed();
            getWorld().setEnemyEnergyRecoverySpeed(currentRecoverySpeed + 0.05f);
        }
        TextSpawner.spawnText("+\nrecovery\nspeed!", Ants.WIDTH / 2, Ants.HEIGHT / 2, getWorld(), Color.WHITE);
    }
}
