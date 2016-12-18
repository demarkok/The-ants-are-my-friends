package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.model.GameWorld;

public class Apple extends Bonus {
    public Apple(float x, float y, GameWorld world) {
        super(x, y, world, "apple2");
    }

    @Override
    public void acceptContact(Ant ant) {
        processContact(ant);
        ant.processContact(this);
    }
}
