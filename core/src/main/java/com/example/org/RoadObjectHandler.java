package com.example.org;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Iterator;

/**
 * Gestiona els objectes de la carretera (monedes, oli, etc.)
 */
public class RoadObjectHandler extends Group {

    private float spawnTimer = 0f;
    private float spawnInterval = 1.6f;

    private final Texture coinTexture, oilPuddleTexture, shieldTexture;
    private final Sound brakeSound, coinSound, shieldSound;
    private float scrollSpeed;

    public RoadObjectHandler(AssetManager assetManager, float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        coinTexture = assetManager.get(AssetDescriptors.coin);
        oilPuddleTexture = assetManager.get(AssetDescriptors.puddle);
        shieldTexture = assetManager.get(AssetDescriptors.shield);
        brakeSound = assetManager.get(AssetDescriptors.carbrake);
        coinSound= assetManager.get(AssetDescriptors.coinSound);
        shieldSound = assetManager.get(AssetDescriptors.shieldsound);
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
     * Genera objectes en carrils lliures
     */
    private void spawnObject() {
        if (getStage() == null) return;

        float y = getStage().getViewport().getWorldHeight();

        // mateixos carrils que cotxes
        float[] lanes = {340, 470, 600, 730};

        float x = lanes[MathUtils.random(0, lanes.length - 1)];

        // REQUISIT: Perquè sembli que estan estàtics, la velocitat ha de ser la de la carretera (scrollSpeed)
        float speedY = scrollSpeed;

        // Triar tipus d'objecte
        int random = MathUtils.random(9);

        RoadObject object;

        if (random < 5) {
            // 50% monedas
            object = new Coin(coinTexture, coinSound, x, y, speedY);

        } else if (random < 9) {
            // 30% aceite
            object = new OilPuddle(oilPuddleTexture, brakeSound, x, y, speedY);

        } else {
            // 20% escudo
            object = new Shield(shieldTexture, shieldSound, x, y, speedY);
        }

        addActor(object);
    }
    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    /**
     * Col·lisions amb el jugador
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
