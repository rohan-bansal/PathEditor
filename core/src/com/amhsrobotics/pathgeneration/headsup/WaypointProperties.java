package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.Main;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.cameramechanics.DigitFilter;
import com.amhsrobotics.pathgeneration.cameramechanics.ModifiedShapeRenderer;
import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.field.Waypoint;
import com.amhsrobotics.pathgeneration.parametrics.ParametricConstants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class WaypointProperties {

    private Waypoint w;

    private BitmapFont font;
    private ModifiedShapeRenderer renderer;
    private GlyphLayout layout = new GlyphLayout();

    private TextField[] entryboxes;
    private String[] sFields = new String[] {"X Position", "Y Position"};

    private Vector2 temp;

    public WaypointProperties(Waypoint w) {
        this.w = w;

        font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
        renderer = new ModifiedShapeRenderer();
        renderer.setColor(Color.TAN);

        entryboxes = new TextField[2];

        for(int x = 0; x < entryboxes.length; x++) {
            entryboxes[x] = new TextField("", ParametricConstants.SKIN);
            entryboxes[x].setPosition(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 300 - (80 * x));
            entryboxes[x].setTextFieldFilter(new DigitFilter());

            Main.stage.addActor(entryboxes[x]);
        }
    }

    public void render(SpriteBatch batch, CameraController cam) {

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        drawFields(batch);

        batch.end();

        if(!FieldConstants.REAL_ZERO.equals(w.getPosition())) {

            renderer.setProjectionMatrix(cam.getCamera().combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.roundedRect(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 500, 150, 40, 5);
            renderer.end();

            batch.setProjectionMatrix(cam.getCamera().combined);
            batch.begin();
            font.setColor(Color.BLACK);
            font.getData().setScale(0.7f);
            layout.setText(font, "Set To Zero");
            font.draw(batch, "Set To Zero", ((Gdx.graphics.getWidth() - 195) + 150 / 2) - layout.width / 2, Gdx.graphics.getHeight() - 474);
            batch.end();

            if(new Rectangle(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 500, 150, 40).contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    FieldConstants.REAL_ZERO.set(w.getPosition());
                }
            }
        }
    }

    public void drawFields(SpriteBatch batch) {

        for(int x = 0; x < entryboxes.length; x++) {
            font.setColor(Color.GOLDENROD);
            font.getData().setScale(0.6f);
            layout.setText(font, sFields[x]);
            font.draw(batch, sFields[x], (entryboxes[x].getX() + entryboxes[x].getWidth() / 2) - layout.width / 2, entryboxes[x].getY() + 70);

            if(!entryboxes[x].hasKeyboardFocus()) {
                Vector2 temp = new Vector2(FieldConstants.getInchVector(w.getPosition()).x, FieldConstants.getInchVector(w.getPosition()).y);
                switch(sFields[x]) {
                    case "X Position":
                        entryboxes[x].setText(temp.x + "");
                        break;
                    case "Y Position":
                        entryboxes[x].setText(temp.y + "");
                        break;
                }
            } else {
                if(sFields[x].equals("X Position")) {
                    try {
                        Float.parseFloat(entryboxes[x].getText());
                        temp = new Vector2(Float.parseFloat(entryboxes[x].getText()), w.getPosition().y);
                    } catch (Exception e) {
                        temp = new Vector2(0, w.getPosition().y);
                    }
                    w.setPositionFromInches(temp);
                } else if(sFields[x].equals("Y Position")) {
                    try {
                        Float.parseFloat(entryboxes[x].getText());
                        temp = new Vector2(w.getPosition().x, Float.parseFloat(entryboxes[x].getText()));
                    } catch (Exception e) {
                        temp = new Vector2(w.getPosition().x, 0);
                    }
                    w.setPositionFromInches(temp);
                }
            }

            entryboxes[x].draw(batch, 1f);
        }
    }
}
