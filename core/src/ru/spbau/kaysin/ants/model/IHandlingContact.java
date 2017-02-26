package ru.spbau.kaysin.ants.model;

import ru.spbau.kaysin.ants.entities.Ant;

public interface IHandlingContact {
    boolean haveContact(Ant ant);
    void acceptContact(Ant ant);
}
