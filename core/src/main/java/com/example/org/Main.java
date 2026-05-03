package com.example.org;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Classe principal del joc
 */
public class Main extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public BitmapFont scoreFont;
    public FitViewport viewport;
    public AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // Carga de todos los assets centralizada
        assetManager.load(AssetDescriptors.bgMusic);
        assetManager.load(AssetDescriptors.crash);
        assetManager.load(AssetDescriptors.carbrake);
        assetManager.load(AssetDescriptors.background);
        assetManager.load(AssetDescriptors.splash);
        assetManager.load(AssetDescriptors.menu);
        assetManager.load(AssetDescriptors.car1);
        assetManager.load(AssetDescriptors.car2);
        assetManager.load(AssetDescriptors.car3);
        assetManager.load(AssetDescriptors.car4);
        assetManager.load(AssetDescriptors.car5);
        assetManager.load(AssetDescriptors.car6);
        assetManager.load(AssetDescriptors.car7);
        assetManager.load(AssetDescriptors.playerCar);
        assetManager.load(AssetDescriptors.playerCar2);
        assetManager.load(AssetDescriptors.playerCar3);
        assetManager.load(AssetDescriptors.puddle);
        assetManager.load(AssetDescriptors.coin);
        assetManager.load(AssetDescriptors.finishline);
        assetManager.load(AssetDescriptors.coinSound);
        assetManager.load(AssetDescriptors.gameover);



        // Bloqueamos hasta que esté cargado
        assetManager.finishLoading();

        // Font personalitzada
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                com.badlogic.gdx.Gdx.files.internal("Quantico-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 70;          // tamaño
        parameter.borderWidth = 3;    // borde
        parameter.borderColor = com.badlogic.gdx.graphics.Color.BLACK;

        scoreFont = generator.generateFont(parameter);

        generator.dispose();

        viewport = new FitViewport(1080, 2400);

        this.setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (font != null) font.dispose();
        scoreFont.dispose();
        assetManager.dispose();
    }
}
