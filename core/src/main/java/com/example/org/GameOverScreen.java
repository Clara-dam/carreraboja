package com.example.org;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Pantalla de Game Over (i Victòria)
 */
public class GameOverScreen implements Screen {

    final Main game;
    private final int score;
    private final boolean win;
    Texture gameover;

    public GameOverScreen(Main game, int score, boolean win) {
        this.game = game;
        this.score = score;
        this.win = win;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        gameover = game.assetManager.get(AssetDescriptors.gameover);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.batch.draw(
            gameover,
            0,
            0,
            game.viewport.getWorldWidth(),
            game.viewport.getWorldHeight()
        );

        game.scoreFont.getData().setScale(1.1f);
        String resultMessage;

        if (win) {
            resultMessage = ">> VICTÒRIA <<\nHas dominat la carrera";
        } else {
            resultMessage = ">> GAME OVER <<\nTorna-ho a intentar";
        }

        game.scoreFont.draw(game.batch, resultMessage,
            0, game.viewport.getWorldHeight() * 0.90f,
            game.viewport.getWorldWidth(), Align.center, true);

        game.scoreFont.getData().setScale(1f);
        game.scoreFont.draw(game.batch, "Puntuació: " + score,
            0, game.viewport.getWorldHeight() * 0.80f,
            game.viewport.getWorldWidth(), Align.center, true);

        game.scoreFont.draw(game.batch, "Toca per tornar al menú",
            0, game.viewport.getWorldHeight() * 0.65f,
            game.viewport.getWorldWidth(), Align.center, true);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void dispose() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
