package com.example.org;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Pantalla de joc amb lògica de meta i distància.
 */
public class GameScreen implements Screen {

    final Main game;
    final Stage stage;

    Music bgMusic;
    PlayerCar player;
    Texture background;
    CarHandler cars;
    RoadObjectHandler objects;

    // --- Lògica de Meta ---
    float distanceTraveled = 0;
    float raceDistance = 100000; // Distància total a recórrer
    boolean raceFinished = false;
    boolean goalReached = false;
    Image finishLine;

    // --- Variables de moviment del background ---
    float backgroundY = 0;
    float scrollSpeed = 1300;

    public GameScreen(final Main game) {
        this.game = game;
        // Usamos el mismo Viewport de la clase principal para consistencia
        stage = new Stage(game.viewport);
    }

    @Override
    public void show() {
        background = game.assetManager.get(AssetDescriptors.background);

        player = new PlayerCar(
            game.assetManager.get(AssetDescriptors.playerCar),
            game.assetManager.get(AssetDescriptors.playerCar2),
            game.assetManager.get(AssetDescriptors.playerCar3)
        );

        cars = new CarHandler(game.assetManager, scrollSpeed);
        objects = new RoadObjectHandler(game.assetManager, scrollSpeed);

        // Creem l'actor de la línia de meta
        finishLine = new Image(game.assetManager.get(AssetDescriptors.finishline));
        finishLine.setWidth(game.viewport.getWorldWidth());
        finishLine.setHeight(250);
        // La posicionem molt amunt, fora de la vista inicial
        finishLine.setPosition(0, game.viewport.getWorldHeight() + 1000);

        stage.addActor(cars);
        stage.addActor(objects);
        stage.addActor(player);
        stage.addActor(finishLine);

        Gdx.input.setInputProcessor(new InputHandler(this));

        bgMusic = game.assetManager.get(AssetDescriptors.bgMusic);
        if (bgMusic != null) {
            bgMusic.setLooping(true);
            bgMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Movimiento del fondo
        backgroundY -= scrollSpeed * delta;
        // Si la imagen ha bajado tanto que ya no se ve, la reseteamos
        if (backgroundY <= -game.viewport.getWorldHeight()) {
            backgroundY = 0;
        }

        // --- GESTIÓ DE LA DISTÀNCIA I META ---
        if (!raceFinished) {
            distanceTraveled += scrollSpeed * delta;
            if (distanceTraveled >= raceDistance) {
                raceFinished = true;
                cars.setSpawningEnabled(false); // Deixem de generar cotxes
            }
        } else if (!goalReached) {
            // La meta baixa cap al jugador
            finishLine.moveBy(0, -scrollSpeed * delta);

            // Si el jugador "creua" la meta
            if (finishLine.getY() < player.getY() + player.getHeight()) {
                goalReached = true;
                if (bgMusic != null) bgMusic.stop();
                // Saltem a GameOverScreen amb el paràmetre win = true
                game.setScreen(new GameOverScreen(game, player.getScore(), true));
                dispose();
                return;
            }
        }

        stage.act(delta);

        // Col·lisions
        cars.hitPlayer(player);
        objects.checkCollision(player);

        // Aplicamos el viewport antes de dibujar
        game.viewport.apply();
        stage.getBatch().setProjectionMatrix(game.viewport.getCamera().combined);
        stage.getBatch().begin();

        // Dibuixar fons infinit
        float width = game.viewport.getWorldWidth();
        float height = game.viewport.getWorldHeight();

        // Dibujamos la primera imagen
        stage.getBatch().draw(background, 0, backgroundY, width, height);
        // Dibujamos la segunda justo encima de la primera
        stage.getBatch().draw(background, 0, backgroundY + height, width, height);

        // UI
        game.scoreFont.getData().setScale(0.8f);
        game.scoreFont.draw(stage.getBatch(), "Vides: " + player.getLives(), 30, height - 50);
        game.scoreFont.draw(stage.getBatch(), "Punts: " + player.getScore(), 30, height - 150);

        // Indicador de progrés
        float progress = Math.min(distanceTraveled / raceDistance, 1.0f);
        game.scoreFont.draw(stage.getBatch(), "Meta: " + (int)(progress * 100) + "%", width - 310, height - 50);

        stage.getBatch().end();
        stage.draw();

        // GAME OVER (Derrota)
        if (player.isDead()) {
            if (bgMusic != null) bgMusic.stop();
            // Saltem a GameOverScreen amb el paràmetre win = false
            game.setScreen(new GameOverScreen(game, player.getScore(), false));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { stage.dispose(); }
}
