package com.example.org;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Escudo que protege al jugador temporalmente
 */
public class Shield extends RoadObject {
    private final Sound shieldSound;

    public Shield(Texture texture, Sound sound, float x, float y, float speedY) {
        super(texture, x, y, speedY);
        this.shieldSound = sound;
    }

    @Override
    public void applyEffect(PlayerCar player) {
        player.activateShield(3f);

        if (shieldSound != null) {
            shieldSound.play();
        }
    }
}
