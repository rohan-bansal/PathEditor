package com.amhsrobotics.pathgeneration.parametrics.abstractions;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.headsup.SplineProperties;
import com.amhsrobotics.pathgeneration.parametrics.libraries.Path;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class SplineController {

    public abstract void setID(int ID);

    public abstract int getID();

    public abstract String getName();

    public abstract ArrayList<Handle> getHandles();

    public abstract void update(SpriteBatch batch, ShapeRenderer renderer, CameraController cam);

    public abstract void generate();

    public abstract void removeSegment(int index);

    public abstract void addSegment(int index, Transform t);

    public abstract Vector2 getCenter();

    public abstract void resetHandles();

    public abstract SplineProperties getProperties();

    public abstract Path getPath();

    public abstract void drawProperties(SpriteBatch batch, CameraController cam);

    public abstract Transform getTransform(int index);

    public abstract void setHandleToTransform();
}
