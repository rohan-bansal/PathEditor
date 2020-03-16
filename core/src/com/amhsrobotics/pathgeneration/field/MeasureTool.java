package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.headsup.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.text.DecimalFormat;

public class MeasureTool {

    private boolean enable = false;
    private Vector2 begin = null, end = null;

    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();
    private DecimalFormat format = new DecimalFormat("##.00");

    private ShapeRenderer renderer;

    public MeasureTool() {
        this.font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(0.7f);

        renderer = new ShapeRenderer();
        renderer.setColor(Color.TAN);
    }

    public void enable() {
        enable = true;
        Overlay.panel.isClickEnabled = true;
    }

    private void disable(CameraController cam) {
        enable = false;
        cam.setPan(true);
        Overlay.panel.isClickEnabled = false;

        begin = null;
        end = null;
    }

    public void render(SpriteBatch batch, CameraController cam) {

        if(enable) {
            Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.getCamera().unproject(unproj);

            cam.setPan(false);

            renderer.setProjectionMatrix(cam.getCamera().combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);

            boolean en2 = true;
            for(Button b : Overlay.buttonManager.getButtons()) {
                if(b.hovered) {
                    en2 = false;
                }
            }

            if(en2) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    begin = new Vector2(unproj.x, unproj.y);
                    for(Waypoint w : Overlay.waypointManager.getWaypoints()) {
                        if(w.hovered) { //align measure tool to waypoint
                            begin = new Vector2(w.getPosition().x, w.getPosition().y);
                        }
                    }
                } else if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                    end = new Vector2(unproj.x, unproj.y);
                    for(Waypoint w : Overlay.waypointManager.getWaypoints()) {
                        if(w.hovered) { //align measure tool to waypoint
                            end = new Vector2(w.getPosition().x, w.getPosition().y);
                        }
                    }
                } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    disable(cam);
                }
            }

            if(begin != null && end == null) {
                renderer.rectLine(begin, new Vector2(unproj.x, unproj.y), 3);
            } else if(begin == null && end != null) {
                renderer.rectLine(new Vector2(unproj.x, unproj.y), end, 3);
            } else if(begin != null && end != null) {
                renderer.rectLine(begin, end, 3);
                batch.setProjectionMatrix(cam.getCamera().combined);
                batch.begin();
                layout.setText(font, format.format(inchesBetweenPoints(begin, end)) + "in");
                Rectangle temp = new Rectangle(begin.x, begin.y, end.x - begin.x, end.y - begin.y);
                font.draw(batch, format.format(inchesBetweenPoints(begin, end)) + "in", begin.x + ((end.x - begin.x) / 2) - layout.width / 2, temp.getY() + temp.getHeight() / 2);
                batch.end();
            }

            renderer.end();
        }
    }

    private float inchesBetweenPoints(Vector2 startIN, Vector2 endIN) {

        float locationInInchesX = (startIN.x * FieldConstants.CALIBRATED_INCH_WIDTH) / Overlay.fieldManager.getPixelWidth();
        float locationInInchesY = (startIN.y * FieldConstants.CALIBRATED_INCH_HEIGHT) / Overlay.fieldManager.getPixelHeight();

        float locationInInchesX2 = (endIN.x * FieldConstants.CALIBRATED_INCH_WIDTH) / Overlay.fieldManager.getPixelWidth();
        float locationInInchesY2 = (endIN.y * FieldConstants.CALIBRATED_INCH_HEIGHT) / Overlay.fieldManager.getPixelHeight();

        return (float) Math.hypot(locationInInchesX2 - locationInInchesX, locationInInchesY2 - locationInInchesY);
    }
}
