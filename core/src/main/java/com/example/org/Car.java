package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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

        // Comprovar si hi ha un cotxe davant en el mateix carril per evitar solapaments
        if (getParent() instanceof Group) {
            Group parent = (Group) getParent();
            for (int i = 0; i < parent.getChildren().size; i++) {
                Actor actor = parent.getChildren().get(i);
                if (actor instanceof Car && actor != this) {
                    Car other = (Car) actor;

                    // Mateix carril (X aproximada)
                    if (Math.abs(getX() - other.getX()) < 10) {
                        // Si "this" està a dalt (Y major) i s'apropa massa a "other" que està a baix
                        float distance = getY() - (other.getY() + other.getHeight());
                        if (distance > 0 && distance < 50) {
                            // Si anem més ràpid que el de davant, igualem velocitat per no trepitjar-lo
                            if (this.speedY > other.speedY) {
                                this.speedY = other.speedY;
                            }
                        }
                    }
                }
            }
        }

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
