package com.amhsrobotics.pathgeneration.positioning;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.field.Waypoint;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.positioning.library.Position;
import com.amhsrobotics.pathgeneration.positioning.library.Rotation;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class Handle {

    private Transform transform;
    private SplineController superClass;
    private Sprite s;
    public boolean hover = false;

    public Handle(Transform t, SplineController splineController) {

        this.transform = t;
        this.superClass = splineController;

        this.s = new Sprite(new Texture(Gdx.files.internal("buttons/handle.png")));

        s.setCenter((float) t.getPosition().getX(), (float) t.getPosition().getY());
    }

    public void render(SpriteBatch batch, CameraController cam) {
        this.s.draw(batch);

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        if(this.s.getBoundingRectangle().contains(unproj.x, unproj.y)) {
            hover = true;
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

                if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    for(Waypoint w : Overlay.waypointManager.getWaypoints()) {
                        if(w.getRect().contains(unproj.x, unproj.y)) {
                            this.s.setCenter(w.getPosition().x, w.getPosition().y);
                            this.transform.setPosition(new Position(w.getPosition().x, w.getPosition().y));
                        }
                    }
                } else {
                    this.s.setCenter(unproj.x, unproj.y);
                    this.transform.setPosition(new Position(unproj.x, unproj.y));

                    if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
                        if(this.transform.getRotation().getHeading() == 0) {
                            this.transform.setRotation(new Rotation(360));
                        }
                        this.transform.setRotation(new Rotation(this.transform.getRotation().getHeading() - 2));
                    } else if(Gdx.input.isKeyPressed(Input.Keys.X)) {
                        if(this.transform.getRotation().getHeading() == 360) {
                            this.transform.setRotation(new Rotation(0));
                        }
                        this.transform.setRotation(new Rotation(this.transform.getRotation().getHeading() + 2));
                    }
                }
            }
            superClass.generate();
        } else {
            hover = false;
        }
    }

    public void setAll() {
        this.transform.setPosition(new Position(s.getX() + s.getWidth() / 2, s.getY() + s.getHeight() / 2));
        superClass.generate();
    }

    public void setAllReverse() {
        s.setCenter((float) transform.getPosition().getX(), (float) transform.getPosition().getY());
    }

    public Rectangle getRectangle() {
        return s.getBoundingRectangle();
    }

    public void remove() {
        superClass.getHandles().remove(this);
    }
}
