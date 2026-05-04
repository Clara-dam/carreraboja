package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Classe base per als objectes de la carretera (monedes, oli, etc.)
 */
public abstract class RoadObject extends Image {

    protected float speedY;

    public RoadObject(Texture texture, float x, float y, float speedY) {
        super(texture);

        // Mantener proporción como en Car
        float aspectRatio = texture.getHeight() / (float) texture.getWidth();
        float width = 80; // un poco más pequeño que los coches
        float height = width * aspectRatio;

        setSize(width, height);

        // Centramos en el carril
        setPosition(x - width / 2f, y);

        this.speedY = speedY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Movimiento hacia abajo
        moveBy(0, -speedY * delta);

        if (getY() < -getHeight()) {
            remove();
        }
    }

    public abstract void applyEffect(PlayerCar player);

    /**
     * Para colisiones
     */
    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
