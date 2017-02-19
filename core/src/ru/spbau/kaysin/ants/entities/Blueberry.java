package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.GameWorld;
import ru.spbau.kaysin.ants.utils.TextSpawner;


public class Blueberry extends DeferredBonus {
    public Blueberry(float x, float y) {
        super(x, y, "blueberry");
    }
    @Override
    public void acceptContact(Ant ant) {
        ant.processContact(this);
    }

    @Override
    public void activate() {
        float currentRecoverySpeed = getWorld().getEnergyRecoverySpeed();
        getWorld().setEnergyRecoverySpeed(currentRecoverySpeed + 0.05f);
        TextSpawner.spawnText("+\nrecovery\nspeed!", Ants.WIDTH / 2, Ants.HEIGHT / 2, getWorld());
    }
}
