package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.fileutils.FileProcessor;
import com.amhsrobotics.pathgeneration.parametrics.CubicController;
import com.amhsrobotics.pathgeneration.parametrics.QuinticController;

import javax.swing.*;

public class ButtonFunctions {

    public static final int QUINTIC_HERMITE = 1, CUBIC_HERMITE = 2, IMPORT = 3, EXPORT = 4, MEASURE = 5, WAYPOINT = 6, FIND_POINT = 7;

    private static FileProcessor processor = new FileProcessor();

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
        } else if(function == EXPORT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JFileChooser chooser = new JFileChooser();
                    JFrame f = new JFrame();
                    f.setVisible(true);
                    f.toFront();
                    f.setVisible(false);
                    int res = chooser.showSaveDialog(f);
                    f.dispose();
                    if (res == JFileChooser.APPROVE_OPTION) {
                        processor.export(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }).start();
        }
    }
}
