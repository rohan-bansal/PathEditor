package com.amhsrobotics.pathgeneration.parametrics.abstractions;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithVelocity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public abstract class SplineController {

    public abstract void setID(int ID);

    public abstract int getID();

    public abstract ArrayList<Handle> getHandles();

    public abstract void update(SpriteBatch batch, ShapeRenderer renderer, CameraController cam);

    public abstract void generate();
}
