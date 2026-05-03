package com.example.org;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Moneda que suma puntuación
 */
public class Coin extends RoadObject {
    private final Sound coinSound;

    public Coin(Texture texture, Sound coinSound, float x, float y, float speedY) {
        super(texture, x, y, speedY);
        this.coinSound = coinSound;
    }

    @Override
    public void applyEffect(PlayerCar player) {
        player.addScore(10);
        if (coinSound != null) {
            coinSound.play();
        }
    }
}
