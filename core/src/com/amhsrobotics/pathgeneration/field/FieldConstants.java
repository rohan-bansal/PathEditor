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
}
