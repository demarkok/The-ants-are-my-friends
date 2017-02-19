package ru.spbau.kaysin.ants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.Tween;
import ru.spbau.kaysin.ants.screens.MenuScreen;
import ru.spbau.kaysin.ants.screens.PlayScreen;
import ru.spbau.kaysin.ants.utils.TweenActor;

// SINGLETON
public class Ants extends Game {

    // virtual size
    public static final int WIDTH = 540;
    public static final int HEIGHT = 960;
    public static final String TITLE = "Ants";

    private static AssetManager assets = new AssetManager();
    private static FreeTypeFontGenerator generator;

    private Ants() {
    }

    private static class SingletonHolder {
        private static final Ants INSTANCE = new Ants();

    }

    public static Ants getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static AssetManager getAssets() {
        return assets;
    }

    public static FreeTypeFontGenerator getGenerator() {
        return generator;
    }

    @Override
    public void create () {

        generator = new FreeTypeFontGenerator(Gdx.files.internal("FONTS/visitor1.ttf"));
        assets.load("pack.txt", TextureAtlas.class);
        assets.load("explosion.txt", TextureAtlas.class);
        assets.finishLoading();
        assets.update();

        Tween.registerAccessor(Actor.class, new TweenActor());


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

    @Override
    public void setScreen(Screen screen) {
        if (this.screen instanceof PlayScreen) {
            this.screen.dispose();
        }
        super.setScreen(screen);
    }
}
