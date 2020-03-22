package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.headsup.WaypointProperties;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.text.DecimalFormat;

public class Waypoint {

    private Sprite sprite;
    private Vector2 realPosition;
    private Vector2 centeredPosition;

    public boolean hovered = false;

    private int ID;

    private GlyphLayout layout = new GlyphLayout();
    private DecimalFormat format = new DecimalFormat("##.00");

    private WaypointProperties properties;

    public Waypoint(float x, float y, int ID, boolean isInInches) {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("field/waypoint.png")));
        this.sprite.setSize(48, 48);

        this.ID = ID;

        if(isInInches) {
            Vector2 t = FieldConstants.getImaginaryVector(new Vector2(x, y));
            realPosition = new Vector2(t.x, t.y);
            centeredPosition = new Vector2(realPosition.x - sprite.getWidth() / 2, realPosition.y - 4);
        } else {
            realPosition = new Vector2(x, y);
            centeredPosition = new Vector2(realPosition.x - sprite.getWidth() / 2, realPosition.y - 4);
        }


        properties = new WaypointProperties(this);
    }

    public Vector2 getPosition() {
        return realPosition;
    }

    public void render(SpriteBatch batch, CameraController cam, BitmapFont font) {

        Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(unproj);

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        sprite.setPosition(centeredPosition.x, centeredPosition.y);
        sprite.draw(batch);
        if(sprite.getBoundingRectangle().contains(unproj.x, unproj.y)) {
            this.hovered = true;
            this.layout.setText(font, "(" + format.format(FieldConstants.getInchVector(realPosition).x) + ", " + format.format(FieldConstants.getInchVector(realPosition).y) + ")");
            font.setColor(Color.GOLDENROD);
            font.getData().setScale(0.7f);
            font.draw(batch, "(" + format.format(FieldConstants.getInchVector(realPosition).x) + ", " + format.format(FieldConstants.getInchVector(realPosition).y) + ")", (sprite.getX() + sprite.getWidth() / 2) - layout.width / 2, sprite.getY() + 60);
        } else {
            this.hovered = false;
        }

        batch.end();
    }

    public Rectangle getRect() {
        return sprite.getBoundingRectangle();
    }

    public int getID() {
        return ID;
    }

    public void drawProperties(SpriteBatch batch, CameraController cam) {
        this.properties.render(batch, cam);
    }

    public void setPositionFromInches(Vector2 vec) {
        Vector2 t = FieldConstants.getImaginaryVector(new Vector2(vec.x, vec.y));
        realPosition = new Vector2(t.x, t.y);
        centeredPosition = new Vector2(realPosition.x - sprite.getWidth() / 2, realPosition.y - 4);
    }
}
