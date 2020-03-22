package com.amhsrobotics.pathgeneration.fileutils;


import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;

import java.util.ArrayList;

public class pgData {

    ArrayList<SplineController> splines = new ArrayList<>();

    public void setSplines(ArrayList<SplineController> splines) {
        this.splines = splines;
    }

    public ArrayList<SplineController> getSplines() {
        return splines;
    }
}
