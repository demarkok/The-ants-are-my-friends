package ru.spbau.kaysin.ants.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.spbau.kaysin.ants.Ants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Ants.WIDTH;
		config.height = Ants.HEIGHT;
		config.title = Ants.TITLE;
		new LwjglApplication(Ants.getInstance(), config);

	}
}
