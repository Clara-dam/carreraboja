package com.example.org;

import com.badlogic.gdx.graphics.Texture;

public class FrontCar extends Car {

    public FrontCar(Texture texture, float x, float y, float speedY) {
        // Li passem una velocitat base multiplicada per 4 per fer-lo més ràpid i simular que ve cap a nosaltres
        // això juntament a girar la imatge fa l'efecte
        super(texture, x, y, speedY * 4f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
