package com.amhsrobotics.pathgeneration;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.field.FieldGraphManager;
import com.amhsrobotics.pathgeneration.parametrics.CubicController;
import com.amhsrobotics.pathgeneration.parametrics.QuinticController;
import com.amhsrobotics.pathgeneration.parametrics.SplineManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Overlay {

    public static FieldGraphManager fieldManager;
    public static SplineManager splineManager;

    public Overlay() {

        fieldManager = new FieldGraphManager();
        splineManager = new SplineManager();

        splineManager.addSpline(new QuinticController());
    }

    public void updateAll(SpriteBatch batch, CameraController worldCam, CameraController hudCam) {


        Vector3 vec = new Vector3( Gdx.input.getX(), Gdx.input.getY(), 0 );
        worldCam.getCamera().unproject(vec);


        fieldManager.render(batch, worldCam);
        splineManager.drawAll(batch, worldCam);

        if(fieldManager.getField().getBoundingRectangle().contains(vec.x, vec.y)) {
            if(Gdx.input.justTouched()) {
                Gdx.app.log(FieldConstants.getInchVector(new Vector2(vec.x, vec.y)) + "", FieldConstants.getImaginaryVector(FieldConstants.getInchVector(new Vector2(vec.x, vec.y))) + "");
            }
        }

    }
}
