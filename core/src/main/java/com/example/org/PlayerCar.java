package com.example.org;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * El jugador juga com un cotxe únic
 * Té 3 vides inicials i pot quedar congelat
 */
public class PlayerCar extends Image {

    private int lives = 3;
    private float freezeTime = 0;
    private final Texture normalTexture, damagedTexture, criticalTexture;
    private float damageEffectTime = 0;

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

        if (lives >= 3) {
            setDrawable(new TextureRegionDrawable(normalTexture));
        } else if (lives == 2) {
            setDrawable(new TextureRegionDrawable(damagedTexture));
        } else if (lives <= 1) {
            setDrawable(new TextureRegionDrawable(criticalTexture));
        }
    }

    public void takeDamage(int amount) {
        lives -= amount;
        if (lives < 0) lives = 0;
    }
    public void damageEffect() {
        damageEffectTime = 0.2f;
    }

    public void freeze(float seconds) {
        freezeTime = seconds;
    }
    public void heal(int amount) {
        lives += amount;
        if (lives > 10) lives = 10;
    }

    public boolean isFrozen() {
        return freezeTime > 0;
    }

    public int getLives() {
        return lives;
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
