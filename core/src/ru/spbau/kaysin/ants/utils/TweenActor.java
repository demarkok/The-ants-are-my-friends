package ru.spbau.kaysin.ants.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;


public class TweenActor implements TweenAccessor<Actor> {

    public static final int POSITION_XY = 1;

    @Override
    public int getValues(Actor actor, int i, float[] returnValues) {
        if (i == POSITION_XY) {
            returnValues[0] = actor.getX();
            returnValues[1] = actor.getY();
            return 2;
        }
        return 0;
    }

    @Override
    public void setValues(Actor actor, int i, float[] newValues) {
        if (i == POSITION_XY) {
            actor.setPosition(newValues[0], newValues[1]);
        }
    }
}
