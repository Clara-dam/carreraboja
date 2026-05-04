package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * El jugador juga com un cotxe únic
 * Té 3 vides inicials, té puntuació, pot relliscar i tenir escut
 */
public class PlayerCar extends Image {
    private int lives = 3;
    private int score = 0;
    private float shieldTime = 0; // Temps que el cotxe està sota l'efecte del escut
    private int nextLifeScore = 100; // Puntuació per a la propera vida extra
    private float slipperyTime = 0; // Temps que el cotxe està sota l'efecte de l'oli

    private final Texture normalTexture, damagedTexture, criticalTexture;
    private final Texture shieldEffectTexture;

    public PlayerCar(Texture texture, Texture damagedTexture, Texture criticalTexture, Texture shieldEffectTexture) {
        super(texture);
        this.normalTexture = texture;
        this.damagedTexture = damagedTexture;
        this.criticalTexture = criticalTexture;
        this.shieldEffectTexture = shieldEffectTexture;

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

        // Reduir temps d'escut
        if (shieldTime > 0) {
            shieldTime -= delta;
        }

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

        // Dibuixem l'efecte d'escut si està actiu a sobre del cotxe
        if (hasShield() && shieldEffectTexture != null) {
            batch.draw(shieldEffectTexture, getX() - 15, getY() - 10, getWidth() + 30, getHeight() + 25);
        }
    }

    // ------------ VIDAS --------------
    public void takeDamage(int amount) {
        if (hasShield()) return; // no recibe daño con escudo
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

    // ------------ ESCUDO ------------
    public void activateShield(float seconds) {
        shieldTime = seconds;
    }
    public boolean hasShield() {
        return shieldTime > 0;
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
