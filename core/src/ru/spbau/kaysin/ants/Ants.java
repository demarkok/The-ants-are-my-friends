package ru.spbau.kaysin.ants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.spbau.kaysin.ants.screens.PlayScreen;

public class Ants extends Game {

    public static final int WIDTH = 540;
    public static final int HEIGHT = 960;
	public static final String TITLE = "Ants";

    public static AssetManager getAssets() {
        return assets;
    }

    private static AssetManager assets = new AssetManager();

	
	@Override
	public void create () {

        assets.load("pack.txt", TextureAtlas.class);
        assets.finishLoading();
        assets.update();

        setScreen(new PlayScreen());
	}

	@Override
	public void render () {
		super.render();
	}

    @Override public void dispose () {
        super.dispose();
        assets.dispose();
    }
}
