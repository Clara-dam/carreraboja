package com.example.org;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Pantalla de menú inicial
 */
public class MainMenuScreen implements Screen {

    final Main game;
    Texture background;

    public MainMenuScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        background = game.assetManager.get(AssetDescriptors.menu);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.batch.draw(background, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        // Agrupamos las instrucciones en un bloque
        String instructions = "Esquiva els cotxes\n" +
            "i arriba a la meta.";

        game.scoreFont.draw(game.batch, instructions,
            0, worldHeight * 0.60f, worldWidth, Align.center, true);

        // Toca para comenzar
        game.scoreFont.draw(game.batch, "Toca per començar!",
            0, worldHeight * 0.50f, worldWidth, Align.center, true);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
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
    @Override public void dispose() { }
}
