package ru.spbau.kaysin.ants.model;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface HandlingContact {
    void processContact(HandlingContact actor);
    boolean haveContact(HandlingContact actor);
}
