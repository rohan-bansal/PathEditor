package com.amhsrobotics.pathgeneration.parametrics.libraries;

import com.amhsrobotics.pathgeneration.parametrics.abstractions.Parametric;
import com.amhsrobotics.pathgeneration.parametrics.splines.CubicHermiteSpline;
import com.amhsrobotics.pathgeneration.parametrics.splines.QuinticHermiteSpline;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithVelocity;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithVelocityAndCurvature;

public class PathGenerator {
    private static PathGenerator instance = new PathGenerator();

    public static PathGenerator getInstance() {
        return instance;
    }

    public Parametric[] generateCubicHermiteSplinePath(TransformWithVelocity[] waypoints) {
        Parametric[] parametrics = new Parametric[waypoints.length - 1];
        for (int i = 0; i < parametrics.length; i++) {
            parametrics[i] = new CubicHermiteSpline(waypoints[i], waypoints[i + 1]);
        }
        return parametrics;
    }

    public Parametric[] generateQuinticHermiteSplinePath(TransformWithVelocityAndCurvature[] waypoints) {
        Parametric[] parametrics = new Parametric[waypoints.length - 1];

        for (int i = 0; i < parametrics.length; i++) {
            parametrics[i] = new QuinticHermiteSpline(waypoints[i], waypoints[i + 1]);
        }
        return parametrics;
    }

    public Parametric[] generateQuinticHermiteSplinePath(Transform[] waypoints) {
        Parametric[] parametrics = new Parametric[waypoints.length - 1];

        for (int i = 0; i < parametrics.length; i++) {
            parametrics[i] = new QuinticHermiteSpline(waypoints[i], waypoints[i + 1]);
        }
        return parametrics;
    }
}

