package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import ru.spbau.kaysin.ants.Ants;


public class AnthillDomain extends Actor {
    private Sprite texture;

    private GlyphLayout textLayout;
    private BitmapFont font;
    private final String text = "NEW ANT";

    public AnthillDomain(BitmapFont font) {
        this.font = font;
        textLayout = new GlyphLayout(font, text);
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("source"));
        setTouchable(Touchable.enabled);
    }

    // TODO get rid of init
    public void init() {
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        setOrigin(Align.bottomLeft);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        // draw the text in the center of rectangle
        font.draw(batch, textLayout, getX(Align.center) - textLayout.width / 2,
                                     getY(Align.center));

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }
}
