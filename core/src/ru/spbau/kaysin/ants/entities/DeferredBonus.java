package ru.spbau.kaysin.ants.entities;

import ru.spbau.kaysin.ants.model.GameWorld;

// the bonus that should be brought to Codomain for activation
// influences for global state, not for only one Ant.
public abstract class DeferredBonus extends Bonus {
    public DeferredBonus(float x, float y, GameWorld world, String textureName) {
        super(x, y, world, textureName);
    }

    public abstract void activate();
}
