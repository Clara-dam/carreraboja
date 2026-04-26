package com.example.org;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Splash Screen (pantalla inicial)
 */
public class SplashScreen implements Screen {

    final Main game;
    Texture splash;
    float timeShown = 0;

    public SplashScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        splash = game.assetManager.get(AssetDescriptors.splash);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        timeShown += delta;

        // Actualizamos y aplicamos el viewport
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(splash, 0, 0,
            game.viewport.getWorldWidth(),
            game.viewport.getWorldHeight());

        game.batch.end();

        if (timeShown > 3) { // 3 segundos
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
    }
}
