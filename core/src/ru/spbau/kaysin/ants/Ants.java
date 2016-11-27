package ru.spbau.kaysin.ants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import ru.spbau.kaysin.ants.screens.MenuScreen;

// SINGLETON
public class Ants extends Game {

    // virtual size
    public static final int WIDTH = 540;
    public static final int HEIGHT = 960;
    public static final String TITLE = "Ants";

    private static AssetManager assets = new AssetManager();
    private static FreeTypeFontGenerator generator;

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

    public static AssetManager getAssets() {
        return assets;
    }

    public static FreeTypeFontGenerator getGenerator() {
        return generator;
    }

    @Override
    public void create () {
        instance = this;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("FONTS/visitor1.ttf"));
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
        generator.dispose();
    }

}
