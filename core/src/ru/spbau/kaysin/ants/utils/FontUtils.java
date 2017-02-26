package ru.spbau.kaysin.ants.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ru.spbau.kaysin.ants.Ants;

public final class FontUtils {
    public static BitmapFont getFont(Color color, int size) {
        // font
        FreeTypeFontGenerator generator = Ants.getGenerator();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        return generator.generateFont(parameter);
    }
}
