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
    private float carInterval = 3f; // comença en 3 segundos

    // dificultad
    private float difficultyTimer = 0f;

    private final Texture car1, car2, car3, car4, car5, car6, car7;

    //private final Sound fireHit, iceHit;

    public CarHandler(AssetManager assetManager) {
        car1 = assetManager.get(AssetDescriptors.car1);
        car2 = assetManager.get(AssetDescriptors.car2);
        car3 = assetManager.get(AssetDescriptors.car3);
        car4 = assetManager.get(AssetDescriptors.car4);
        car5 = assetManager.get(AssetDescriptors.car5);
        car6 = assetManager.get(AssetDescriptors.car6);
        car7 = assetManager.get(AssetDescriptors.car7);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        spawnTimer += delta;
        difficultyTimer += delta;

        // spawn de cotxes
        if (spawnTimer >= carInterval) {
            spawnCar();
            spawnTimer = 0f;
        }

        // dificultat progressiva
        if (difficultyTimer >= 3f) { // cada 5 segons
            difficultyTimer = 0f;

            if (carInterval > 0.75f) { // mínimo: 1 coche cada 2 segundos
                carInterval -= 0.3f;
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

                // mateix carril (aprox) - comparem el centro del cotxe amb el carril candidate
                if (Math.abs((car.getX() + car.getWidth() / 2f) - laneX) < 10) {

                    // si hi ha un cotxe a prop en vertical, bloqueja
                    if (Math.abs(car.getY() - spawnY) < 150) {
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
        float y = getStage().getViewport().getWorldHeight(); // part superior de la pantalla

        // 4 carrils centrats (deixa marges als costats)
        float[] lanes = {340, 470, 600, 730};

        float x = -1;

        // intentamos encontrar carril libre
        for (int i = 0; i < 10; i++) {
            float candidate = lanes[MathUtils.random(0, lanes.length - 1)];

            if (isLaneFree(candidate, y)) {
                x = candidate;
                break;
            }
        }

        // si no hi ha carril lliure, no generem cotxe
        if (x == -1) return;

        float speedY = MathUtils.random(700, 2000); // velocitat aleatòria entre 200 i 400

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
     * Reprodueix el so correcte segons tipus de spell
     */
    public boolean hitPlayer(PlayerCar player) {
        boolean hit = false;

        Iterator<Actor> it = getChildren().iterator();

        while (it.hasNext()) {
            Actor actor = it.next();
            if (actor instanceof Car) {
                Car car = (Car) actor;

                if (car.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                    player.takeDamage(1);
                    it.remove(); // Eliminación segura durante la iteración
                    hit = true;
                }
            }
        }

        return hit;
    }
}
