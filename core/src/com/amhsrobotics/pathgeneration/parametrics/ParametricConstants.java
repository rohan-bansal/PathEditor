package com.amhsrobotics.pathgeneration.parametrics;

import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithVelocity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ParametricConstants {

    public static final int LINE_WIDTH = 5;

    public static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal("skin/Particle Park UI.atlas"));
    public static final Skin SKIN = new Skin(Gdx.files.internal("skin/Particle Park UI.json"), ATLAS);

    public static TransformWithVelocity[] STARTING_POINTS_CUBIC = new TransformWithVelocity[] {
            new TransformWithVelocity(new Transform(FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).x, FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).y, 180)),
            new TransformWithVelocity(new Transform(FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).x, FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).y, 180)),
    };

    public static Transform[] STARTING_POINTS_QUINTIC = new Transform[] {
            new Transform(FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).x, FieldConstants.getImaginaryVector(new Vector2(0f, -67.25f)).y, 180),
            new Transform(FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).x, FieldConstants.getImaginaryVector(new Vector2(-86.63f, -134.155f)).y, 180),
            new Transform(FieldConstants.getImaginaryVector(new Vector2(-259.49f, -134.155f)).x, FieldConstants.getImaginaryVector(new Vector2(-259.49f, -134.155f)).y, 180)
    };
}
