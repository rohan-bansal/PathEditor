package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.headsup.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class WaypointManager {

    private BitmapFont font;
    private boolean enable = false;

    private ArrayList<Waypoint> waypoints;

    private int currentID = 1;

    public WaypointManager() {
        this.font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));

        waypoints = new ArrayList<>();
        FieldConstants.loadDefaultWaypoints(this);
    }

    public void render(SpriteBatch batch, CameraController cam) {

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        if(enable) {

            cam.setPan(false);

            boolean en2 = true;
            for(Button b : Overlay.buttonManager.getButtons()) {
                if (b.hovered) {
                    en2 = false;
                    break;
                }
            }

            if(en2) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    addWaypointWithPixels(unproj.x, unproj.y);
                } else if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                    for(int x = 0; x < waypoints.size(); x++) {
                        if(waypoints.get(x).getRect().contains(unproj.x, unproj.y)) {
                            Overlay.waypointSelected = 0;
                            waypoints.remove(x);
                        }
                    }
                } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    disableClick(cam);
                }
            }
        }

        for(Waypoint w : waypoints) {
            w.render(batch, cam, font);
            if(w.getRect().contains(unproj.x, unproj.y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Overlay.waypointSelected = w.getID();
                Overlay.splineSelected = 0;
            }
        }

    }

    public void addWaypointWithClick() {
        enable = true;
        Overlay.panel.isClickEnabled = true;
    }

    private void disableClick(CameraController cam) {
        enable = false;
        cam.setPan(true);
        Overlay.panel.isClickEnabled = false;
    }

    public void addWaypointWithInches(float x, float y) {
        this.waypoints.add(new Waypoint(x, y, currentID, true));
        currentID++;
    }

    public void addWaypointWithPixels(float x, float y) {
        this.waypoints.add(new Waypoint(x, y, currentID, false));
        currentID++;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public Waypoint getWaypointByID(int ID) {
        for(Waypoint w : waypoints) {
            if(w.getID() == ID) {
                return w;
            }
        }
        return null;
    }
}
