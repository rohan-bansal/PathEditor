package com.amhsrobotics.pathgeneration.field;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.headsup.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Field;
import java.text.DecimalFormat;


public class FindPoint {

    private boolean enable = false;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();

    private Vector2 position = new Vector2();

    private Sprite dot;

    private DecimalFormat format = new DecimalFormat("##.00");

    public FindPoint() {
        this.dot = new Sprite(new Texture(Gdx.files.internal("field/dot.png")));
        this.font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
    }
    
    public void find() {
        enable = true;
        Overlay.panel.isClickEnabled = true;
    }

    private void disable(CameraController cam) {
        enable = false;
        cam.setPan(true);
        Overlay.panel.isClickEnabled = false;
    }

    public void render(SpriteBatch batch, CameraController cam) {

        if(enable) {
            cam.setPan(false);
            Vector3 unproj = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.getCamera().unproject(unproj);

            boolean en2 = true;
            for(Button b : Overlay.buttonManager.getButtons()) {
                if(b.hovered) {
                    en2 = false;
                }
            }

            if(en2) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    this.dot.setCenter(unproj.x, unproj.y);
                    this.position.set(unproj.x, unproj.y);
                } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    disable(cam);
                }
            }

            batch.begin();
            this.dot.draw(batch);
            this.layout.setText(font, "(" + format.format(FieldConstants.getInchVector(position).x) + ", " + format.format(FieldConstants.getInchVector(position).y) + ")");
            this.font.draw(batch, "(" + format.format(FieldConstants.getInchVector(position).x) + ", " + format.format(FieldConstants.getInchVector(position).y) + ")", (dot.getX() + dot.getWidth() / 2) - layout.width / 2, this.dot.getY() + 50);
            batch.end();
        }
    }

}
