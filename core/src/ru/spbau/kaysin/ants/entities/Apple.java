package ru.spbau.kaysin.ants.entities;


public class Apple extends Bonus {
    public Apple(float x, float y) {
        super(x, y, "apple2");
    }

    @Override
    public void acceptContact(Ant ant) {
        super.acceptContact(ant);
        ant.processContact(this);
    }
}
