package com.amhsrobotics.pathgeneration;

import com.amhsrobotics.pathgeneration.field.FindPoint;
import com.amhsrobotics.pathgeneration.field.MeasureTool;
import com.amhsrobotics.pathgeneration.field.WaypointManager;
import com.amhsrobotics.pathgeneration.headsup.ButtonManager;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.field.FieldGraphManager;
import com.amhsrobotics.pathgeneration.headsup.Panel;
import com.amhsrobotics.pathgeneration.parametrics.SplineManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Overlay {

    public static FieldGraphManager fieldManager;
    public static SplineManager splineManager;
    public static ButtonManager buttonManager;
    public static Panel panel;
    public static FindPoint findPoint;
    public static WaypointManager waypointManager;
    public static MeasureTool measureTool;

    public static int splineSelected = 0;
    public static int waypointSelected = 0;

    public Overlay() {

        fieldManager = new FieldGraphManager();
        splineManager = new SplineManager();
        buttonManager = new ButtonManager();
        waypointManager = new WaypointManager();
        measureTool = new MeasureTool();

        panel = new Panel();
        findPoint = new FindPoint();
    }

    public void updateAll(SpriteBatch batch, CameraController worldCam, CameraController hudCam) {
        
        fieldManager.render(batch, worldCam);
        splineManager.drawAll(batch, worldCam);
        findPoint.render(batch, worldCam);
        waypointManager.render(batch, worldCam);
        measureTool.render(batch, worldCam);
        panel.render(batch, hudCam);

        if(!panel.hidden) {
            if(splineManager.getSplineByID(splineSelected) != null) {
                splineManager.getSplineByID(splineSelected).drawProperties(batch, hudCam);
            } else if(waypointManager.getWaypointByID(waypointSelected) != null) {
                waypointManager.getWaypointByID(waypointSelected).drawProperties(batch, hudCam);
            }
        }

        buttonManager.updateAll(batch, hudCam);

    }
}
