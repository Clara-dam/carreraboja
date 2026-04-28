package com.example.org;

import com.badlogic.gdx.graphics.Texture;

/**
 * Moneda que suma puntuación
 */
public class Coin extends RoadObject {

    public Coin(Texture texture, float x, float y, float speedY) {
        super(texture, x, y, speedY);
    }

    @Override
    public void applyEffect(PlayerCar player) {
        player.addScore(10);
    }
}
