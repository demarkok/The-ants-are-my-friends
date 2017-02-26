package ru.spbau.kaysin.ants;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    public static final float WIDTH_PADDING = Ants.WIDTH / 15;
    public static final float HEIGHT_PADDING = Ants.HEIGHT / 15;
    public static final float START_MOVEMENT_FINE = 0.15f; // decrease the energy when the dragging starts
    public static final float ENERGY_CONSUMPTION = 0.0005f;
    public static final Color CAPTURE_COLOR = new Color(189 / 255f, 194 / 248f, 155 / 222f, 1);
    public static final Color PLAYBACK_COLOR = new Color(200 / 255f, 194 / 248f, 155 / 222f, 1);
    public static final int MENU_TEXT_SIZE = 50;
    public static final String DEFAULT_HOST_IP = "46.101.228.47";
}
