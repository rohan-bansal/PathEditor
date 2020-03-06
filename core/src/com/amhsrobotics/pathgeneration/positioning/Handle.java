package com.amhsrobotics.pathgeneration.positioning;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.positioning.library.Position;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Handle {

    private Transform transform;
    private SplineController superClass;
    private Sprite s;

    public Handle(Transform t, SplineController splineController) {

        this.transform = t;
        this.superClass = splineController;

        this.s = new Sprite(new Texture(Gdx.files.internal("buttons/handle.png")));

        s.setCenter((float) t.getPosition().getX(), (float) t.getPosition().getY());
    }

    public void render(SpriteBatch batch, CameraController cam) {
        if(this.transform != null) {
            this.s.draw(batch);

            Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.getCamera().unproject(unproj);

            if(this.s.getBoundingRectangle().contains(unproj.x, unproj.y)) {
                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    this.s.setCenter(unproj.x, unproj.y);
                    this.transform.setPosition(new Position(unproj.x, unproj.y));
                    superClass.generate();
                }
            }
        } else {
            superClass.getHandles().remove(this);
        }
    }
}
