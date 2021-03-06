package ru.spbau.kaysin.ants.utils;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.entities.CharEntity;
import ru.spbau.kaysin.ants.model.GameWorld;

import java.util.ArrayList;


public class TextSpawner {

    private static final Vector2 UP_VECTOR = new Vector2(-1, 0);
    private static final Vector2 DOWN_VECTOR = new Vector2(1 ,0);


    public static void spawnText(String text, float posX, float posY, GameWorld gameWorld, Color color) {

        final ArrayList<CharEntity> charEntities = new ArrayList<CharEntity>(); // for removing chars after showing message

        float currentOffsetX; // offset relatively posX
        float currentOffsetY; // offset relatively posY


        BitmapFont font = FontUtils.getFont(Color.WHITE, 55);
        GlyphLayout layout = new GlyphLayout();

        Vector2 position = new Vector2();
        Vector2 enterPosition = new Vector2();
        Vector2 exitPosition = new Vector2();

        String[] lines = text.split("\n");
        currentOffsetY = font.getLineHeight() * lines.length;

        for (String line: lines) {

            layout.setText(font, line);
            currentOffsetX = -layout.width / 2;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == ' ') {
                    currentOffsetX += layout.width / line.length() / 2;
                    continue;
                }

                position.set(currentOffsetX + posX, currentOffsetY + posY);

                Vector2 currVector = new Vector2();

                if (i % 2 == 0) {
                    enterPosition.set(UP_VECTOR).scl(Ants.HEIGHT / 2 * 1.5f).add(position);
                    exitPosition.set(DOWN_VECTOR).scl(Ants.HEIGHT / 2 * 1.5f).add(position);

                    currVector.set(UP_VECTOR);
                } else {
                    enterPosition.set(DOWN_VECTOR).scl(Ants.HEIGHT / 2 * 1.5f).add(position);
                    exitPosition.set(UP_VECTOR).scl(Ants.HEIGHT / 2 * 1.5f).add(position);

                    currVector.set(DOWN_VECTOR);
                }


                CharEntity charEnt = new CharEntity(String.valueOf(c), enterPosition.x, enterPosition.y, font, color);
                charEntities.add(charEnt);

                Timeline.createSequence()
                        .push(Tween.to(charEnt, TweenActor.POSITION_XY, 1.0f).target(position.x, position.y).ease(TweenEquations.easeOutQuart))
                        .pushPause(1)
                        .push(Tween.to(charEnt, TweenActor.POSITION_XY, 1.0f).target(exitPosition.x, exitPosition.y).ease(TweenEquations.easeInQuart))
                        .delay(0.1f * i)
                        .pushPause(0.08f * text.length())
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                for (CharEntity charEnt : charEntities) {
                                    charEnt.remove();
                                }
                            }
                        }
                        )
                        .start(gameWorld.getTweenManager());

                gameWorld.getStage().addActor(charEnt);
                currentOffsetX += charEnt.getWidth();
            }
            currentOffsetY -= font.getLineHeight();
        }




    }
}
