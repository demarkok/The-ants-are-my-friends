package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.model.GameWorld;


public class Blueberry extends Bonus {
    public Blueberry(float x, float y, GameWorld world) {
        super(x, y, world, "blueberry");
    }
    @Override
    public void acceptContact(Ant ant) {
        processContact(ant);
        ant.processContact(this);
    }
}
