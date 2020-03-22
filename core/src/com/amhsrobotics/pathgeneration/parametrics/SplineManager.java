package com.amhsrobotics.pathgeneration.parametrics;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.SplineSelector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import sun.java2d.pipe.AlphaColorPipe;

import java.util.ArrayList;

public class SplineManager {

    private ArrayList<SplineController> splines;
    private ArrayList<SplineSelector> selectSpline;

    private ShapeRenderer renderer;

    private int ID = 1;

    public SplineManager() {
        this.splines = new ArrayList<>();
        selectSpline = new ArrayList<>();

        renderer = new ShapeRenderer();
    }

    public void drawAll(SpriteBatch batch, CameraController cam) {

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        for(SplineController s : splines) {
            s.update(batch, renderer, cam);
        }

        batch.begin();
        for(SplineSelector selector : selectSpline) {
            if(selector.getSpline().getID() != Overlay.splineSelected) {
                selector.draw(batch);
            }
            if(selector.getBoundingRectangle().contains(unproj.x, unproj.y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Overlay.splineSelected = 0;
                Overlay.waypointSelected = 0;
                Overlay.splineSelected = selector.getSpline().getID();
                getSplineByID(Overlay.splineSelected).resetHandles();
            }
        }
        batch.end();
    }

    public void addSpline(SplineController s) {
        s.setID(ID);
        splines.add(s);
        ID += 1;

        Overlay.splineSelected = 0;

        selectSpline.add(new SplineSelector(s));
        s.generate();
    }

    public SplineController getSplineByID(int ID) {
        for(SplineController s : splines) {
            if(s.getID() == ID) {
                return s;
            }
        }
        return null;
    }

    public void writeAll(FileHandle handle) {
        for(SplineController s : splines) {
            s.writeTo(handle);
        }
    }
}
