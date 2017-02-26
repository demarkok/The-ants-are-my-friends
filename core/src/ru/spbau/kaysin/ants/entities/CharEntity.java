package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;


public class CharEntity extends Actor {

    private String text;
    private Color color;
    private BitmapFont font;
    private GlyphLayout layout;

    public CharEntity(String text, float x, float y, BitmapFont font, Color color) {
        setPosition(x, y);
        this.text = text;
        this.font = font;
        this.color = color;

        font.setUseIntegerPositions(false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        layout = new GlyphLayout();
        layout.setText(font, text);

        setSize(layout.width, layout.height);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(color);
        font.draw(batch, text, getX(), getY(), layout.width, Align.topLeft, false);

    }
}
