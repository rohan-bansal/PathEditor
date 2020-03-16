package com.amhsrobotics.pathgeneration.positioning;

import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SplineSelector extends Sprite {

    private SplineController c;

    public SplineSelector(SplineController spline) {
        super(new Texture(Gdx.files.internal("buttons/select-spline.png")));
        this.c = spline;

        setCenter(c.getCenter().x, c.getCenter().y);
        setSize(48, 48);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        if(getX() != c.getCenter().x && getY() != c.getCenter().y) {
            setCenter(c.getCenter().x, c.getCenter().y);
        }
    }

    public SplineController getSpline() {
        return c;
    }
}
