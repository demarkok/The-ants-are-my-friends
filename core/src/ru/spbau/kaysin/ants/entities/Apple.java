package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.model.GameWorld;


public class Apple extends Bonus {
    public Apple(float x, float y) {
        super(x, y, "apple2");
    }

    @Override
    public void acceptContact(Ant ant) {
        ant.processContact(this);
    }
}
