package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * El jugador juga com un cotxe únic
 * Té 3 vides inicials, té puntuació, pot tenir escut temporal
 */
public class PlayerCar extends Image {
    private int lives = 3;
    private int score = 0;

    private float shieldTime = 0;
    private final Texture normalTexture, damagedTexture, criticalTexture;

    public PlayerCar(Texture texture, Texture damagedTexture, Texture criticalTexture) {
        super(texture);
        this.normalTexture = texture;
        this.damagedTexture = damagedTexture;
        this.criticalTexture = criticalTexture;

        float aspectRatio = texture.getHeight() / (float) texture.getWidth();
        float width = 110;
        float height = width * aspectRatio;

        setSize(width, height);
        setPosition(800 / 2 - getWidth() / 2, 40);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Reducir tiempo de escudo
        if (shieldTime > 0) {
            shieldTime -= delta;
        }

        if (lives >= 3) {
            setDrawable(new TextureRegionDrawable(normalTexture));
        } else if (lives == 2) {
            setDrawable(new TextureRegionDrawable(damagedTexture));
        } else if (lives <= 1) {
            setDrawable(new TextureRegionDrawable(criticalTexture));
        }
    }
    // ------------ VIDAS --------------
    public void takeDamage(int amount) {
        if (hasShield()) return; // no recibe daño con escudo

        lives -= amount;
        if (lives < 0) lives = 0;
    }
    public int getLives() {
        return lives;
    }
    public boolean isDead() {
        return lives <= 0;
    }

    // ------------ PUNTUACIÓN ------------
    public void addScore(int amount) {
        score += amount;
    }
    public int getScore() {
        return score;
    }
    // ------------ ESCUDO ------------
    public void activateShield(float seconds) {
        shieldTime = seconds;
    }

    public boolean hasShield() {
        return shieldTime > 0;
    }
    // ------------ COLISIONES ------------
    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
