package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.parametrics.CubicController;
import com.amhsrobotics.pathgeneration.parametrics.QuinticController;

public class ButtonFunctions {

    public static final int QUINTIC_HERMITE = 1, CUBIC_HERMITE = 2, IMPORT = 3, EXPORT = 4, MEASURE = 5, WAYPOINT = 6, FIND_POINT = 7;

    public static void process(int function) {
        if(function == QUINTIC_HERMITE) {
            Overlay.splineManager.addSpline(new QuinticController());
        } else if(function == CUBIC_HERMITE) {
            Overlay.splineManager.addSpline(new CubicController());
        } else if(function == MEASURE) {
            Overlay.measureTool.enable();
        } else if(function == WAYPOINT) {
            Overlay.waypointManager.addWaypointWithClick();
        } else if(function == FIND_POINT) {
            Overlay.findPoint.find();
        }
    }
}
