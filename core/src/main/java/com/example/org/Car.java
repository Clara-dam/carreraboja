package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Classe base per als cotxes enemics
 * Es mouen de dalt cap avall i poden danyar el jugador
 */
public class Car extends Image {
    protected float speedY;

    public Car(Texture texture, float x, float y, float speedY) {
        super(texture);

        float aspectRatio = texture.getHeight() / (float) texture.getWidth();
        float width = 100;
        float height = width * aspectRatio;

        setSize(width, height);
        // Centramos el coche: la X que recibimos es el centro del carril
        setPosition(x - width / 2f, y);

        this.speedY = speedY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        moveBy(0, -speedY * delta); // Moviment cap avall

        // Si surt per sota de la pantalla, s'elimina
        if (getY() < -getHeight()) {
            remove();
        }
    }

    /**
     * Retorna un rectangle per calcular col·lisions
     */
    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
