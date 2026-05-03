package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * El jugador juga com un cotxe únic
 * Té 3 vides inicials, té puntuació, pot relliscar
 */
public class PlayerCar extends Image {
    private int lives = 3;
    private int score = 0;
    private int nextLifeScore = 100; // Puntuació per a la propera vida extra
    private float slipperyTime = 0; // Temps que el cotxe està sota l'efecte de l'oli

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
        // Establecer el origen en el centro para que las rotaciones queden bien
        setOrigin(width / 2f, height / 2f);
        setPosition(1080 / 2f - getWidth() / 2, 40);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Gestionar el efecto de patinar (aceite)
        if (slipperyTime > 0) {
            slipperyTime -= delta;
            // Efecto visual de rotación para simular pérdida de control
            setRotation((float) Math.sin(System.currentTimeMillis() * 0.015) * 20f);
        } else {
            setRotation(0);
        }

        updateTexture();
    }

    private void updateTexture() {
        if (lives >= 3) {
            setDrawable(new TextureRegionDrawable(normalTexture));
        } else if (lives == 2) {
            setDrawable(new TextureRegionDrawable(damagedTexture));
        } else if (lives <= 1) {
            setDrawable(new TextureRegionDrawable(criticalTexture));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    // ------------ VIDAS --------------
    public void takeDamage(int amount) {
        lives -= amount;
        if (lives < 0) lives = 0;
        updateTexture();
    }

    public void addLife() {
        lives++;
        updateTexture();
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

        // REQUISITO: Cada 100 puntos gana una vida
        if (score >= nextLifeScore) {
            addLife();
            nextLifeScore += 100;
        }
    }
    public int getScore() {
        return score;
    }

    // ------------ CHARCO DE ACEITE ------------
    public void makeSlippery(float seconds) {
        this.slipperyTime = seconds;
    }

    public boolean isSlippery() {
        return slipperyTime > 0;
    }

    // ------------ COLISIONES ------------
    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
