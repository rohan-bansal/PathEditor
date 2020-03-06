package com.amhsrobotics.pathgeneration.parametrics;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.parametrics.libraries.Path;
import com.amhsrobotics.pathgeneration.parametrics.libraries.PathGenerator;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class QuinticController extends SplineController {

    private int ID;

    private ArrayList<Transform> transforms;
    private ArrayList<Vector2> currentSpline;
    private ArrayList<Handle> splineHandles;

    private Path path;

    public QuinticController(Transform[] transforms) {
        this.transforms = new ArrayList<>();
        this.currentSpline = new ArrayList<>();
        this.splineHandles = new ArrayList<>();

        this.transforms.addAll(Arrays.asList(transforms));

        for(Transform t : this.transforms) {
            splineHandles.add(new Handle(t, this));
        }

        generate();
    }

    public QuinticController() {
        this(new Transform[] {
                new Transform(FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).x, FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).y, 180),
                new Transform(FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).x, FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).y, 180),
                new Transform(FieldConstants.getImaginaryVector(new Vector2(-259.49f, -134.155f)).x, FieldConstants.getImaginaryVector(new Vector2(-259.49f, -134.155f)).y, 180)
        });
    }

    @Override
    public void generate() {

        this.path = new Path(PathGenerator.getInstance().generateQuinticHermiteSplinePath(this.transforms.toArray(new Transform[0])));

        currentSpline.clear();
        for(double a = 0; a < 1.0; a += 0.0001) {
            Vector2 generatedPoint = new Vector2((float) this.path.getPosition(a).getX(), (float) this.path.getPosition(a).getY());
            currentSpline.add(generatedPoint);
        }
    }

    @Override
    public void update(SpriteBatch batch, ShapeRenderer renderer, CameraController cam) {

        renderer.setProjectionMatrix(cam.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw spline
        renderer.setColor(Color.SALMON);
        for(Vector2 vec : currentSpline) {
            renderer.circle(vec.x, vec.y, ParametricConstants.LINE_WIDTH);
        }
        renderer.end();

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        for(Handle h : splineHandles) {
            h.render(batch, cam);
        }
        batch.end();
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public ArrayList<Handle> getHandles() {
        return splineHandles;
    }
}
