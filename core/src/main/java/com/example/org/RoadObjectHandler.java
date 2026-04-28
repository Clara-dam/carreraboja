package com.example.org;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Iterator;

/**
 * Gestiona los objetos de la carretera (monedas, escudos, etc.)
 */
public class RoadObjectHandler extends Group {

    private float spawnTimer = 0f;
    private float spawnInterval = 4f; // menos frecuente que coches

    private final Texture coinTexture;
    private final Texture shieldTexture;

    private final float scrollSpeed;

    public RoadObjectHandler(AssetManager assetManager, float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        coinTexture = assetManager.get(AssetDescriptors.coin);
        shieldTexture = assetManager.get(AssetDescriptors.shield);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        spawnTimer += delta;

        if (spawnTimer >= spawnInterval) {
            spawnObject();
            spawnTimer = 0f;
        }
    }

    /**
     * Genera objetos en carriles libres
     */
    private void spawnObject() {
        if (getStage() == null) return;

        float y = getStage().getViewport().getWorldHeight();

        // mismos carriles que coches
        float[] lanes = {340, 470, 600, 730};

        float x = lanes[MathUtils.random(0, lanes.length - 1)];

        // Velocidad similar a coches pero más lenta
        float forwardSpeed = MathUtils.random(400, 600);
        float speedY = scrollSpeed - forwardSpeed;

        if (speedY < 200) speedY = 200; // evitar que suba o vaya muy lento

        // Elegir tipo de objeto
        int random = MathUtils.random(9);

        RoadObject object;

        if (random < 7) {
            // 70% monedas
            object = new Coin(coinTexture, x, y, speedY);
        } else {
            // 30% escudo
            object = new Shield(shieldTexture, x, y, speedY);
        }

        addActor(object);
    }

    /**
     * Colisiones con el jugador
     */
    public void checkCollision(PlayerCar player) {
        Iterator<Actor> it = getChildren().iterator();

        while (it.hasNext()) {
            Actor actor = it.next();

            if (actor instanceof RoadObject) {
                RoadObject obj = (RoadObject) actor;

                if (obj.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                    obj.applyEffect(player);
                    it.remove(); // eliminación segura
                }
            }
        }
    }
}
