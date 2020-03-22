package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.Main;
import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.cameramechanics.DigitFilter;
import com.amhsrobotics.pathgeneration.cameramechanics.ModifiedShapeRenderer;
import com.amhsrobotics.pathgeneration.field.FieldConstants;
import com.amhsrobotics.pathgeneration.parametrics.ParametricConstants;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.amhsrobotics.pathgeneration.positioning.Handle;
import com.amhsrobotics.pathgeneration.positioning.library.Rotation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class SplineProperties {

    private SplineController spline;
    private ModifiedShapeRenderer renderer;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();
    private Sprite colorpicker;

    private int selectedTransform = 0;

    private Sprite delete;

    private TextField[] fields;
    private String[] sFields;
    public boolean colorpickerOpen = false;

    public SplineProperties(SplineController s, String[] stringFields) {
        this.spline = s;
        this.sFields = stringFields;
        this.fields = new TextField[stringFields.length];

        colorpicker = new Sprite(new Texture(Gdx.files.internal("frames/colorpicker.png")));
        colorpicker.setScale(0.7f);
        colorpicker.setCenter(Gdx.graphics.getWidth() - 125, 150);

        renderer = new ModifiedShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));

        delete = new Sprite(new Texture(Gdx.files.internal("buttons/delete.png")));
        delete.setScale(0.5f);

        for(int x = 0; x < stringFields.length; x++) {
            fields[x] = new TextField("", ParametricConstants.SKIN);
            fields[x].setPosition(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 300 - (80 * x));
            fields[x].setTextFieldFilter(new DigitFilter());

            Main.stage.addActor(fields[x]);
        }
    }

    public void render(SpriteBatch batch, CameraController cam) {

        if(selectedTransform != 0) {
            renderer.setProjectionMatrix(cam.getCamera().combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.roundedRect(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 205, 150, 40, 5);
            renderer.end();

            batch.setProjectionMatrix(cam.getCamera().combined);
            batch.begin();

            font.setColor(Color.BLACK);
            font.getData().setScale(0.7f);
            layout.setText(font, "Transform " + selectedTransform);
            font.draw(batch, "Transform " + selectedTransform, ((Gdx.graphics.getWidth() - 195) + 150 / 2) - layout.width / 2, Gdx.graphics.getHeight() - 205 + 27);

            delete.setCenter(Gdx.graphics.getWidth() - 23, Gdx.graphics.getHeight() - 205 + 20);
            delete.draw(batch);
            if(delete.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                selectedTransform = 0;
            }

            if(selectedTransform > 0) {
                drawFields(batch);
            }
            batch.end();
        } else {
            for(int x = 0; x < spline.getTransforms().size(); x++) {

                renderer.setProjectionMatrix(cam.getCamera().combined);
                renderer.begin(ShapeRenderer.ShapeType.Filled);

                renderer.setColor(Color.LIGHT_GRAY);
                renderer.roundedRect(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 150 - (55 * (x + 1)), 150, 40, 5);

                if(new Rectangle(Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 150 - (55 * (x + 1)), 150, 40).contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        selectedTransform = x + 1;
                    }
                }
                renderer.end();

                batch.setProjectionMatrix(cam.getCamera().combined);
                batch.begin();

                font.setColor(Color.BLACK);
                font.getData().setScale(0.7f);
                layout.setText(font, "Transform " + (x + 1));
                font.draw(batch, "Transform " + (x + 1), ((Gdx.graphics.getWidth() - 195) + 150 / 2) - layout.width / 2, Gdx.graphics.getHeight() - 150 - (55 * (x + 1)) + 27);
                batch.end();
            }
        }

        if(!colorpickerOpen) {
            renderer.setProjectionMatrix(cam.getCamera().combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.TAN);
            renderer.roundedRect(Gdx.graphics.getWidth() - 195, 140, 150, 40, 5);
            renderer.end();

            batch.setProjectionMatrix(cam.getCamera().combined);
            batch.begin();

            font.setColor(Color.BLACK);
            font.getData().setScale(0.7f);
            layout.setText(font, "Color Picker");
            font.draw(batch, "Color Picker", ((Gdx.graphics.getWidth() - 195) + 150 / 2) - layout.width / 2, 165);

            batch.end();

            if(new Rectangle(Gdx.graphics.getWidth() - 195, 140, 150, 40).contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    this.colorpickerOpen = true;
                }
            }
        } else {
            delete.setCenter(Gdx.graphics.getWidth() - 125, 280);
            batch.setProjectionMatrix(cam.getCamera().combined);
            batch.begin();
            colorpicker.draw(batch);
            delete.draw(batch);
            batch.end();

            if(delete.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    colorpickerOpen = false;
                }
            }

            if(colorpicker.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Texture texture = colorpicker.getTexture();

                    int spriteLocalX = (int) (Gdx.input.getX() - colorpicker.getX());
                    int spriteLocalY = (int) ((Gdx.graphics.getHeight() - Gdx.input.getY()) - colorpicker.getY());

                    if (!texture.getTextureData().isPrepared()) {
                        texture.getTextureData().prepare();
                    }

                    Pixmap pixmap = texture.getTextureData().consumePixmap();
                    Overlay.splineManager.getSplineByID(Overlay.splineSelected).setColor(new Color(pixmap.getPixel(spriteLocalX, (int) colorpicker.getHeight() - spriteLocalY)));
                }
            }
        }
    }

    private void drawFields(SpriteBatch batch) {

        for(int x = 0; x < fields.length; x++) {

            if(!fields[x].hasKeyboardFocus()) {
                Vector2 temp = new Vector2((float) spline.getTransforms().get(selectedTransform - 1).getPosition().getX(), (float) spline.getTransforms().get(selectedTransform - 1).getPosition().getY());
                switch(sFields[x]) {
                    case "X Position":
                        fields[x].setText(FieldConstants.getInchVector(temp).x + "");
                        break;
                    case "Y Position":
                        fields[x].setText(FieldConstants.getInchVector(temp).y + "");
                        break;
                    case "Heading":
                        fields[x].setText(spline.getTransforms().get(selectedTransform - 1).getRotation().getHeading() + "");
                        break;
                }
            } else {
                if(sFields[x].equals("X Position")) {
                    Vector2 temp;
                    try {
                        Float.parseFloat(fields[x].getText());
                        temp = new Vector2(Float.parseFloat(fields[x].getText()), (float) spline.getTransform(selectedTransform - 1).getPosition().getY());
                    } catch (Exception e) {
                        temp = new Vector2(0, (float) spline.getTransform(selectedTransform - 1).getPosition().getY());
                    }
                    spline.getTransform(selectedTransform - 1).setX(FieldConstants.getImaginaryVector(temp).x);
                } else if(sFields[x].equals("Y Position")) {
                    Vector2 temp;
                    try {
                        Float.parseFloat(fields[x].getText());
                        temp = new Vector2((float) spline.getTransform(selectedTransform - 1).getPosition().getX(), Float.parseFloat(fields[x].getText()));
                    } catch (Exception e) {
                        temp = new Vector2((float) spline.getTransform(selectedTransform - 1).getPosition().getX(), 0);
                    }
                    spline.getTransform(selectedTransform - 1).setY(FieldConstants.getImaginaryVector(temp).y);
                } else if(sFields[x].equals("Heading")) {
                    try {
                        Float.parseFloat(fields[x].getText());
                        spline.getTransform(selectedTransform - 1).setRotation(new Rotation(Float.parseFloat(fields[x].getText())));
                    } catch (Exception e) {
                        spline.getTransform(selectedTransform - 1).setRotation(new Rotation(0));
                    }
                }
                spline.setHandleToTransform();
                spline.generate();
            }

            fields[x].draw(batch, 1f);
            layout.setText(font, sFields[x]);

            font.setColor(Color.GOLDENROD);
            font.getData().setScale(0.6f);
            font.draw(batch, sFields[x], (fields[x].getX() + fields[x].getWidth() / 2) - layout.width / 2, fields[x].getY() + 70);
        }
    }
}
