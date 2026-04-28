package com.example.org;

import com.badlogic.gdx.graphics.Texture;

/**
 * Escudo que protege al jugador temporalmente
 */
public class Shield extends RoadObject {

    public Shield(Texture texture, float x, float y, float speedY) {
        super(texture, x, y, speedY);
    }

    @Override
    public void applyEffect(PlayerCar player) {
        player.activateShield(3f); // 3 segundos de escudo
        remove();
    }
}
