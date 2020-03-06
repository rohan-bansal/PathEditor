package com.amhsrobotics.pathgeneration.cameramechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraController {

    private OrthographicCamera camera;
    private boolean isUI;
    private boolean pan = true;

    public CameraController(boolean UI) {

        this.isUI = UI;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(!isUI) {
            camera.zoom = 2.239f;
            camera.position.set(1805f, 720, 0f);
        }

        camera.update();
    }

    public void update() {

        camera.update();

        if(!isUI && pan) {
            if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                camera.translate(-Gdx.input.getDeltaX() * 13, Gdx.input.getDeltaY() * 13);
                camera.update();
            }
        }

    }

    public void togglePan() {
        pan = !pan;
    }

    public void setPan(boolean pan) {
        this.pan = pan;
    }

/*    public void fixBounds() {
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        float scaledViewportWidthHalfExtent = viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = viewportHeight * camera.zoom * 0.5f;

        // Horizontal
        if (camera.position.x < scaledViewportWidthHalfExtent)
            camera.position.x = scaledViewportWidthHalfExtent;
        else if (camera.position.x > map.getPixelWidth() - scaledViewportWidthHalfExtent)
            camera.position.x = map.getPixelWidth() - scaledViewportWidthHalfExtent;

        // Vertical
        if (camera.position.y < scaledViewportHeightHalfExtent)
            camera.position.y = scaledViewportHeightHalfExtent;
        else if (camera.position.y > map.getPixelHeight() - scaledViewportHeightHalfExtent)
            camera.position.y = map.getPixelHeight() - scaledViewportHeightHalfExtent;
    }*/

    public OrthographicCamera getCamera() {
        return camera;
    }
}
