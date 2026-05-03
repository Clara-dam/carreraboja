package com.example.org;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Charco de aceite que entorpece el movimiento del jugador
 */
public class OilPuddle extends RoadObject {

    private final Sound brakeSound;

    public OilPuddle(Texture texture, Sound brakeSound, float x, float y, float speedY) {
        super(texture, x, y, speedY);
        this.brakeSound = brakeSound;
    }

    @Override
    public void applyEffect(PlayerCar player) {
        player.makeSlippery(2.5f); // El jugador patina durante 2.5 segundos
        if (brakeSound != null) {
            brakeSound.play();
        }
    }
}
