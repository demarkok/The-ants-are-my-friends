package ru.spbau.kaysin.ants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.spbau.kaysin.ants.screens.MenuScreen;

public class Ants extends Game {

    // SINGLETON
    private static Ants instance = null;

    private Ants() {
    }

    public static Ants getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new Ants();
        }
    }

    // virtual size
    public static final int WIDTH = 540;
    public static final int HEIGHT = 960;
	public static final String TITLE = "Ants";

    public static AssetManager getAssets() {
        return assets;
    }

    private static AssetManager assets = new AssetManager();

	
	@Override
	public void create () {
        instance = this;

        assets.load("pack.txt", TextureAtlas.class);
        assets.finishLoading();
        assets.update();

        setScreen(new MenuScreen());

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
