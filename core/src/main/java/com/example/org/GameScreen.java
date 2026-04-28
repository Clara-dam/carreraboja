package com.example.org;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Pantalla de juego
 */
public class GameScreen implements Screen {

    final Main game;
    final Stage stage;

    Music bgMusic;
    PlayerCar player;
    Texture background;
    CarHandler cars;
    RoadObjectHandler objects;
    float elapsedTime = 0;

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
        // Opcional: Esto ayuda a que no se vea una línea de separación entre las dos imágenes
        //background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        player = new PlayerCar(
            game.assetManager.get(AssetDescriptors.playerCar),
            game.assetManager.get(AssetDescriptors.playerCar2),
            game.assetManager.get(AssetDescriptors.playerCar3)
        );

        cars = new CarHandler(game.assetManager, scrollSpeed);
        objects = new RoadObjectHandler(game.assetManager, scrollSpeed);

        stage.addActor(cars);
        stage.addActor(objects);
        stage.addActor(player);

        Gdx.input.setInputProcessor(new InputHandler(this));

        //bgMusic = game.assetManager.get(AssetDescriptors.bgMusic);
        //bgMusic.setLooping(true);
        //bgMusic.play();
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
        stage.act(delta);

        // Comprobar colisiones
        cars.hitPlayer(player);
        objects.checkCollision(player);

        elapsedTime += delta;

        // Aplicamos el viewport antes de dibujar
        game.viewport.apply();
        stage.getBatch().setProjectionMatrix(game.viewport.getCamera().combined);
        stage.getBatch().begin();

        // --- DIBUJAR FONDO DOBLE PARA EFECTO INFINITO ---
        float width = game.viewport.getWorldWidth();
        float height = game.viewport.getWorldHeight();

        // Dibujamos la primera imagen
        stage.getBatch().draw(background, 0, backgroundY, width, height);
        // Dibujamos la segunda justo encima de la primera
        stage.getBatch().draw(background, 0, backgroundY + height, width, height);

        // Dibujamos la UI
        game.scoreFont.draw(stage.getBatch(), "Vides: " + player.getLives(),
            10, game.viewport.getWorldHeight() - 10);

        game.scoreFont.draw(stage.getBatch(), "Puntuació: " + player.getScore(),
            game.viewport.getWorldWidth() - 300, game.viewport.getWorldHeight() - 10);

        stage.getBatch().end();

        stage.draw();

        // GAME OVER
        /*if (player.isDead()) {
            //bgMusic.stop();
            game.setScreen(new GameOverScreen(game, player.getScore()));
            dispose();
        }*/
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
