package com.amhsrobotics.pathgeneration.parametrics;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class SplineManager {

    private ArrayList<SplineController> splines;

    private ShapeRenderer renderer;

    private int ID = 0;

    public SplineManager() {
        this.splines = new ArrayList<>();

        renderer = new ShapeRenderer();
    }

    public void drawAll(SpriteBatch batch, CameraController cam) {
        for(SplineController s : splines) {
            s.update(batch, renderer, cam);
        }
    }

    public void addSpline(SplineController s) {
        s.setID(ID);
        splines.add(s);
        ID += 1;
    }
}
