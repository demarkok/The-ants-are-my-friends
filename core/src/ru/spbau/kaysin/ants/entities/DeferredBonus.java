package ru.spbau.kaysin.ants.entities;


// the bonus that should be brought to Codomain for activation
// influences on global state, not on the only one Ant.
public abstract class DeferredBonus extends Bonus {
    public DeferredBonus(float x, float y, String textureName) {
        super(x, y, textureName);
    }

    public abstract void activate();
}
