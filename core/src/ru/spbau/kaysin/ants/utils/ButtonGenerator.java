package ru.spbau.kaysin.ants.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import ru.spbau.kaysin.ants.Ants;

public class ButtonGenerator {
    public static TextButton generateButton(String text, int textSize, float posX, float posY) {
        Skin skin = new Skin();
        skin.addRegions(Ants.getAssets().get("pack.txt", TextureAtlas.class));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = FontUtils.getFont(Color.BLACK, textSize);
        textButtonStyle.up = skin.getDrawable("energyBar");
        textButtonStyle.down = skin.getDrawable("backgroundBar");
        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(posX - button.getWidth() / 2, posY - button.getHeight() / 2);
        return button;
    }
}
