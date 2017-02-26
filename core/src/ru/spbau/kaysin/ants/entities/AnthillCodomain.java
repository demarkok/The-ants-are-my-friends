package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import ru.spbau.kaysin.ants.Ants;
import ru.spbau.kaysin.ants.model.IHandlingContact;
import ru.spbau.kaysin.ants.utils.FontUtils;


public class AnthillCodomain extends Actor implements IHandlingContact {
    private Sprite texture;

    private GlyphLayout textLayout;
    private BitmapFont font;
    private final String text = "CODOMAIN";

    private boolean friendly;


    public AnthillCodomain(boolean friendly) {

        font = FontUtils.getFont(Color.BLACK, 18);

        this.friendly = friendly;

        textLayout = new GlyphLayout(font, text);
        texture = new Sprite(Ants.getAssets().get("pack.txt", TextureAtlas.class).findRegion("anthill"));
        setTouchable(Touchable.enabled);
    }

    public void init() {
        setSize(texture.getWidth(), texture.getHeight());

        if (friendly) {
            setPosition(getParent().getWidth() - getWidth(), 0);
        } else {
            setPosition(0, getParent().getHeight() - getHeight());
        }

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
    public boolean haveContact(Ant ant) {
        Rectangle rectangle = new Rectangle(getX(Align.bottomLeft), getY(Align.bottomLeft), getWidth(), getHeight());
        return rectangle.contains(ant.getPosition());
    }

    @Override
    public void acceptContact(Ant ant) {
        ant.processContact(this);
    }


    public boolean isFriendly() {
        return friendly;
    }
}
