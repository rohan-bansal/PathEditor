package com.amhsrobotics.pathgeneration.parametrics;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.headsup.SplineProperties;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.parametrics.libraries.Path;
import com.amhsrobotics.pathgeneration.parametrics.libraries.PathGenerator;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;

public class QuinticController extends SplineController {

    private int ID;

    private ArrayList<Transform> transforms;
    private ArrayList<Vector2> currentSpline;
    private ArrayList<Handle> splineHandles;
    private Sprite[] addSegments = new Sprite[2];
    private ArrayList<Sprite> removeSegments;

    private Path path;
    private SplineProperties properties;

    public String[] fields = new String[] {"X Position", "Y Position", "Heading"};
    private Color color = Color.SALMON;

    public QuinticController(Transform[] transforms) {
        this.transforms = new ArrayList<>();
        this.currentSpline = new ArrayList<>();
        this.splineHandles = new ArrayList<>();
        this.removeSegments = new ArrayList<>();

        this.transforms.addAll(Arrays.asList(transforms));
        addSegments[0] = new Sprite(new Texture("buttons/add-segment.png"));
        addSegments[1] = new Sprite(new Texture("buttons/add-segment.png"));

        properties = new SplineProperties(this, fields);

        refreshHandles();
        generate();
    }

    public QuinticController() {
        this(ParametricConstants.STARTING_POINTS_QUINTIC);
    }

    private void refreshHandles() {
        splineHandles.clear();
        removeSegments.clear();
        for(Transform t : this.transforms) {
            splineHandles.add(new Handle(t, this));

            Sprite s = new Sprite(new Texture("buttons/remove-segment.png"));
            s.setCenter((float) t.getPosition().getX() + 30, (float) t.getPosition().getY() - 8);
            removeSegments.add(s);
        }
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
        renderer.setColor(color);
        for(Vector2 vec : currentSpline) {
            renderer.circle(vec.x, vec.y, ParametricConstants.LINE_WIDTH);
        }
        renderer.end();

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        if(Overlay.splineSelected == getID()) {
            drawHandles(batch, cam);
        }
        batch.end();
    }

    private void drawHandles(SpriteBatch batch, CameraController cam) {

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        for(Handle h : splineHandles) {
            h.render(batch, cam);
        }

        for(int x = 0; x < addSegments.length; x++) {
            addSegments[x].draw(batch);

            if(addSegments[x].getBoundingRectangle().contains(unproj.x, unproj.y)  && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Transform t;
                if(x == 0) {
                    t = new Transform(path.getStartWaypoint().getPosition().getX() + 100, path.getStartWaypoint().getPosition().getY(), 180);
                    addSegment(0, t);
                } else {
                    t = new Transform(path.getEndWaypoint().getPosition().getX() - 100, path.getEndWaypoint().getPosition().getY(), 180);
                    addSegment(this.transforms.size(), t);
                }
                generate();
            }
        }

        for(int x = 0; x < this.transforms.size(); x++) {
            if(this.transforms.get(x) != null) {
                if(x == 0) {
                    removeSegments.get(x).setCenter((float) this.transforms.get(x).getPosition().getX() + 30, (float) this.transforms.get(x).getPosition().getY() - 8);
                } else if(x == this.transforms.size() - 1) {
                    removeSegments.get(x).setCenter((float) this.transforms.get(x).getPosition().getX() - 30, (float) this.transforms.get(x).getPosition().getY() - 8);
                } else {
                    removeSegments.get(x).setCenter((float) this.transforms.get(x).getPosition().getX(), (float) this.transforms.get(x).getPosition().getY() + 30);
                }
                removeSegments.get(x).draw(batch);
                if(removeSegments.get(x).getBoundingRectangle().contains(unproj.x, unproj.y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if(this.transforms.size() != 2) {
                        this.removeSegment(x);
                    }
                }
            }

        }

        addSegments[0].setCenter((float) path.getStartWaypoint().getPosition().getX() + 30, (float) path.getStartWaypoint().getPosition().getY() + 8);
        addSegments[1].setCenter((float) path.getEndWaypoint().getPosition().getX() - 30, (float) path.getEndWaypoint().getPosition().getY() + 8);

    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return "Quintic Hermite";
    }

    @Override
    public ArrayList<Handle> getHandles() {
        return splineHandles;
    }

    @Override
    public void removeSegment(int index) {
        this.transforms.remove(index);
        this.splineHandles.remove(index);
        refreshHandles();
        generate();
    }

    @Override
    public void addSegment(int index, Transform t) {
        this.transforms.add(index, t);
        this.splineHandles.add(new Handle(t, this));

        Sprite s = new Sprite(new Texture("buttons/remove-segment.png"));
        s.setCenter((float) t.getPosition().getX() + 30, (float) t.getPosition().getY() - 8);
        removeSegments.add(s);
    }

    @Override
    public Vector2 getCenter() {
        return new Vector2((float) this.path.getPosition(0.5).getX(), (float) this.path.getPosition(0.5).getY());
    }

    @Override
    public void resetHandles() {
        for(Handle h : splineHandles) {
            h.setAll();
        }
    }

    @Override
    public void drawProperties(SpriteBatch batch, CameraController cam) {
        this.properties.render(batch, cam);
    }

    @Override
    public Transform getTransform(int index) {
        return transforms.get(index);
    }

    @Override
    public void setHandleToTransform() {
        for(Handle h : splineHandles) {
            h.setAllReverse();
        }
    }

    @Override
    public ArrayList<Transform> getTransforms() {
        return transforms;
    }

    @Override
    public void writeTo(FileHandle file) {

        Transform[] transforms = new Transform[this.transforms.size()];
        for(int x = 0; x < this.transforms.size(); x++) {
            Gdx.app.log("Writing:", "Transform " + (x + 1));
            Vector2 temp = FieldConstants.getInchVector(new Vector2(
                    (float) this.transforms.get(x).getPosition().getX(),
                    (float) this.transforms.get(x).getPosition().getY()));
            transforms[x] = new Transform(temp.x, temp.y, this.transforms.get(x).getRotation().getHeading());
        }

        file.writeString("Quintic Hermite ID #" + this.ID + "\n", true);
        file.writeString("new Transform[] {\n", true);
        for(int y = 0; y < this.transforms.size(); y++) {
            file.writeString("\t" + transforms[y].toString() + ",\n", true);
        }
        file.writeString("}", true);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
