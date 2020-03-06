package com.amhsrobotics.pathgeneration.cameramechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputCore implements InputProcessor {

    private CameraController cam;

    public InputCore(CameraController camera) {
        this.cam = camera;
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.RIGHT_BRACKET) {
            cam.getCamera().rotate( 5f);
        } else if(keycode == Input.Keys.LEFT_BRACKET) {
            cam.getCamera().rotate(-5f);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled (int change) {
        Vector3 screenCoords = new Vector3( Gdx.input.getX(), Gdx.input.getY(), 0 );

        Vector3 worldCoordsBefore = cam.getCamera().unproject( new Vector3( screenCoords ) );

        cam.getCamera().zoom += change * cam.getCamera().zoom * 0.1f;
        cam.getCamera().update();

        Vector3 worldCoordsAfter = cam.getCamera().unproject( new Vector3( screenCoords ) );

        Vector3 diff = new Vector3( worldCoordsAfter ).sub( worldCoordsBefore );
        cam.getCamera().position.sub( diff );
        cam.getCamera().update();

        return true;
    }
}

