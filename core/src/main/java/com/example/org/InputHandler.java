package com.example.org;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputHandler implements InputProcessor {

    private final GameScreen gameScreen;

    public InputHandler(GameScreen screen) {
        this.gameScreen = screen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //movePlayer(screenX, screenY);
        //return true;
        //touchDown descartat perquè si no el joc és molt fàcil
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        movePlayer(screenX, screenY);
        return true;
    }

    private void movePlayer(int screenX, int screenY) {
        // REQUISIT: Si el cotxe està lliscant per l'oli, el jugador no pot moure'l
        if (gameScreen.player.isSlippery()) return;

        Vector3 vector = gameScreen.stage.getCamera()
            .unproject(new Vector3(screenX, screenY, 0));

        float playerWidth = gameScreen.player.getWidth();

        // Carriles (los mismos que en CarHandler)
        float[] lanes = {340, 470, 600, 730};

        // Buscar carril más cercano al dedo
        float closestLane = lanes[0];
        float minDistance = Math.abs(vector.x - lanes[0]);

        for (float lane : lanes) {
            float distance = Math.abs(vector.x - lane);
            if (distance < minDistance) {
                minDistance = distance;
                closestLane = lane;
            }
        }

        float x = closestLane - playerWidth / 2;
        float y = gameScreen.player.getY();

        // Aplicamos la posición al jugador
        gameScreen.player.setPosition(x, y);
    }

    // El resto no usado
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}
