package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class FieldConstants {

    public static final float CALIBRATED_INCH_HEIGHT = 323.25f;
    public static final float CALIBRATED_INCH_WIDTH = 629.25f;

    public static Vector2 REAL_ZERO = new Vector2(2051.68f, Overlay.fieldManager.getPixelHeight() / 2); // (0, 0) in pixel


    public static Vector2 getInchZero() {
        float zeroInInchesX = (REAL_ZERO.x * CALIBRATED_INCH_WIDTH) / Overlay.fieldManager.getPixelWidth();
        float zeroInInchesY = (REAL_ZERO.y * CALIBRATED_INCH_HEIGHT) / Overlay.fieldManager.getPixelHeight();

        return new Vector2(zeroInInchesX, zeroInInchesY);
    }

    public static Vector2 getInchVector(Vector2 position) {
        float locationInInchesX = (position.x * CALIBRATED_INCH_WIDTH) / Overlay.fieldManager.getPixelWidth();
        float locationInInchesY = (position.y * CALIBRATED_INCH_HEIGHT) / Overlay.fieldManager.getPixelHeight();

        return new Vector2(locationInInchesX - getInchZero().x, locationInInchesY - getInchZero().y);
    }

    public static Vector2 getImaginaryVector(Vector2 inchPosition) {

        Vector2 vec2 = new Vector2();
        vec2.x = getInchZero().x + inchPosition.x;
        vec2.y = getInchZero().y + inchPosition.y;

        return new Vector2(((vec2.x * Overlay.fieldManager.getPixelWidth()) / CALIBRATED_INCH_WIDTH),
                (vec2.y * Overlay.fieldManager.getPixelHeight()) / CALIBRATED_INCH_HEIGHT);
    }

    public static void loadDefaultWaypoints(WaypointManager manager) {

        manager.addWaypointWithPixels(REAL_ZERO.x, REAL_ZERO.y); // ZERO
        manager.addWaypointWithInches(0, 60.75f); // LOADING_STARTING_POINT
        manager.addWaypointWithInches(0, -67.25f); // SCORING_STARTING_POINT
        manager.addWaypointWithInches(0, -134.155f); // TRENCH_STARTING_POINT
        manager.addWaypointWithInches(-86.63f, -134.155f); // A_TRENCH_FRONT_CENTER
        manager.addWaypointWithInches(-302.63f, -134.155f); // A_TRENCH_BACK_CENTER
        manager.addWaypointWithInches(90f, -67.25f); // SCORING_ZONE_TIP -- unstable


        manager.addWaypointWithInches(-130.25f, -46.05f); // BALL 1
        manager.addWaypointWithInches(-114.94f, -39.71f); // BALL 2
        manager.addWaypointWithInches(-107.83f, -15.3f); // BALL 3
        manager.addWaypointWithInches(-114.17f, 0); // BALL 4
        manager.addWaypointWithInches(-120.51f, 15.3f); // BALL 5

        manager.addWaypointWithInches(-86.63f, 134.155f); // OPPONENT_TRENCH_BACK_CENTER
        manager.addWaypointWithInches(-302.63f, 134.155f); // OPPONENT_TRENCH_FRONT_CENTER

        manager.addWaypointWithInches(-259.49f, -134.155f);
        manager.addWaypointWithInches(-121f, -44.24f);
        manager.addWaypointWithInches(121.144f, -67.25f);
    }
}
