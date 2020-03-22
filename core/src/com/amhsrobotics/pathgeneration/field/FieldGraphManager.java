package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class FieldGraphManager {

    private Sprite field;

    public FieldGraphManager() {
        this.field = new Sprite(new Texture("field/fieldrender.png"));
        this.field.setPosition(0, 0);
    }

    public void render(SpriteBatch batch, CameraController cam) {

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        field.draw(batch);

        batch.end();


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Overlay.splineSelected = 0;
            Overlay.waypointSelected = 0;
        }

        if(Overlay.splineSelected == 0 && Overlay.waypointSelected == 0) {
            Overlay.panel.hide();
        } else {
            Overlay.panel.show();
        }
    }

    public float getPixelWidth() {
        return field.getWidth();
    }

    public float getPixelHeight() {
        return field.getHeight();
    }

    public Vector2 getPixelVector() {
        return new Vector2(getPixelWidth(), getPixelHeight());
    }

    public Sprite getField() {
        return field;
    }
}
