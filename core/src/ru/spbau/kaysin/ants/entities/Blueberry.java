package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.model.GameWorld;


public class Blueberry extends DeferredBonus {
    public Blueberry(float x, float y, GameWorld world) {
        super(x, y, world, "blueberry");
    }
    @Override
    public void acceptContact(Ant ant) {
        ant.processContact(this);
    }

    @Override
    public void activate() {
        float currentRecoverySpeed = getWorld().getEnergyRecoverySpeed();
        getWorld().setEnergyRecoverySpeed(currentRecoverySpeed + 0.05f);
    }
}
