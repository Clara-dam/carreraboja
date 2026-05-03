package com.example.org;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


/**
 * Gestiona els cotxes enemics que circulen per la carretera.
 * Els cotxes apareixen a la part superior i baixen cap avall.
 * Es generen en 4 carrils fixos, deixant espai als costats per decoració.
 * El tipus de cotxe es selecciona aleatòriament entre diversos sprites.
 */
public class CarHandler extends Group {
    // control de spawn en segons
    private float spawnTimer = 0f;
    private float carInterval = 3f; // comienza en 3 segundos
    private float difficultyTimer = 0f; // dificultad
    private final Texture car1, car2, car3, car4, car5, car6, car7;
    private final float scrollSpeed;
    private final Sound crashSound;

    private boolean spawningEnabled = true;

    public CarHandler(AssetManager assetManager, float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        car1 = assetManager.get(AssetDescriptors.car1);
        car2 = assetManager.get(AssetDescriptors.car2);
        car3 = assetManager.get(AssetDescriptors.car3);
        car4 = assetManager.get(AssetDescriptors.car4);
        car5 = assetManager.get(AssetDescriptors.car5);
        car6 = assetManager.get(AssetDescriptors.car6);
        car7 = assetManager.get(AssetDescriptors.car7);
        crashSound = assetManager.get(AssetDescriptors.crash);
    }

    public void setSpawningEnabled(boolean enabled) {
        this.spawningEnabled = enabled;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!spawningEnabled) return;

        spawnTimer += delta;
        difficultyTimer += delta;

        if (spawnTimer >= carInterval) {
            spawnCar();
            spawnTimer = 0f;
        }

        if (difficultyTimer >= 3f) { // Cada 3 segundos aumenta la dificultad (↓ valor = más rápido, ↑ valor = más lento)
            difficultyTimer = 0f;    // Reinicia el contador para volver a medir el tiempo
            if (carInterval > 0.9f) {  // Límite mínimo entre coches (↓ = más difícil, ↑ = más fácil)
                carInterval -= 0.3f; // Reduce el tiempo entre coches (↑ resta = dificultad sube más rápido, ↓ resta = más gradual)
            }
        }
    }
    /**
     * Comprova si un carril està lliure per evitar solapaments
     */
    private boolean isLaneFree(float laneX, float spawnY) {
        for (Actor actor : getChildren()) {
            if (actor instanceof Car) {
                Car car = (Car) actor;
                if (Math.abs((car.getX() + car.getWidth() / 2f) - laneX) < 10) {
                    if (Math.abs(car.getY() - spawnY) < 200) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Genera un cotxe enemic en un dels carrils disponibles.
     * Els carrils estan centrats i no ocupen tota l'amplada de la pantalla.
     */
    private void spawnCar() {
        if (getStage() == null) return;
        float y = getStage().getViewport().getWorldHeight();
        float[] lanes = {340, 470, 600, 730};
        float x = -1;

        for (int i = 0; i < 10; i++) {
            float candidate = lanes[MathUtils.random(0, lanes.length - 1)];
            if (isLaneFree(candidate, y)) {
                x = candidate;
                break;
            }
        }

        // si no hi ha carril lliure, no generem cotxe
        if (x == -1) return;

        // Velocitat relativa: l'enemic es mou per la carretera a una velocitat 'forwardSpeed'
        // Per tant, a la pantalla el veiem baixar a 'scrollSpeed - forwardSpeed'
        float forwardSpeed = MathUtils.random(400, 700);
        float speedY = scrollSpeed - forwardSpeed;

        // Selecció aleatòria entre els 7 cotxes
        Texture selectedTexture;
        int randomCar = MathUtils.random(6);

        switch (randomCar) {
            case 0: selectedTexture = car1; break;
            case 1: selectedTexture = car2; break;
            case 2: selectedTexture = car3; break;
            case 3: selectedTexture = car4; break;
            case 4: selectedTexture = car5; break;
            case 5: selectedTexture = car6; break;
            default: selectedTexture = car7; break;
        }
        Car car = new Car(selectedTexture, x, y, speedY);
        addActor(car);
    }

    /**
     * Comprova col·lisions amb el jugador
     */
    public void hitPlayer(PlayerCar player) {
        Iterator<Actor> it = getChildren().iterator();

        while (it.hasNext()) {
            Actor actor = it.next();
            if (actor instanceof Car) {
                Car car = (Car) actor;

                if (car.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                    player.takeDamage(1);
                    if (crashSound != null) crashSound.play();
                    it.remove();
                }
            }
        }
    }
}
