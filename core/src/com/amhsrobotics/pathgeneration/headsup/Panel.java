package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Panel {

    private ShapeRenderer renderer;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();

    public boolean isClickEnabled = false;

    public boolean hidden = true;
    private Vector2 currentX = new Vector2(Gdx.graphics.getWidth(), 0);
    private Vector2 target = new Vector2(Gdx.graphics.getWidth() - 250, 0);
    private Vector2 currentWidth = new Vector2(0, 0);

    public Panel() {
        this.renderer = new ShapeRenderer();
        this.renderer.setColor(Color.DARK_GRAY);

        this.font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
    }

    public void render(Batch batch, CameraController cam) {

        renderer.setProjectionMatrix(cam.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        this.renderer.setColor(Color.DARK_GRAY);
        renderer.rect(0, 0, 80, Gdx.graphics.getHeight());

        if(!hidden) {
            if(currentX.x > target.x) {
                currentX.lerp(target, 0.1f);
                currentWidth.lerp(new Vector2(250, 0), 0.1f);
                renderer.rect(currentX.x, 0, currentWidth.x, Gdx.graphics.getHeight());
            } else {
                renderer.rect(Gdx.graphics.getWidth() - 250, 0, 250, Gdx.graphics.getHeight());
            }
        } else {
            if(currentX.x < target.x) {
                currentX.lerp(target, 0.1f);
                currentWidth.lerp(new Vector2(0, 0), 0.1f);
                renderer.rect(currentX.x, 0, currentWidth.x, Gdx.graphics.getHeight());
            } else {
                renderer.rect(Gdx.graphics.getWidth(), 0, 0, Gdx.graphics.getHeight());
            }
        }

        if(isClickEnabled) {
            renderer.setColor(Color.FIREBRICK);
            renderer.rectLine(80 + 1.5f, 0, Gdx.graphics.getWidth() + 1.5f, 0, 3);
            renderer.rectLine(80, 0, 80, Gdx.graphics.getHeight(), 3);
            renderer.rectLine(80, Gdx.graphics.getHeight() - 1.5f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 1.5f, 3);
            renderer.rectLine(Gdx.graphics.getWidth() - 1.5f, Gdx.graphics.getHeight(), Gdx.graphics.getWidth() - 1.5f, 0, 3);
        }

        renderer.end();

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        this.font.setColor(Color.TAN);
        this.font.getData().setScale(1f);
        for(Button b : Overlay.buttonManager.getButtons()) {
            if(b.hovered) {
                font.draw(batch, b.getName(), 100, Gdx.graphics.getHeight() - 20);
            }
        }

        if(!hidden) {
            font.setColor(Color.SALMON);
            font.getData().setScale(1f);

            layout.setText(font, "Properties");
            font.draw(batch, "Properties", (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 40);

            font.setColor(Color.LIGHT_GRAY);
            font.getData().setScale(0.5f);

            layout.setText(font, "'ESC' To Close");
            font.draw(batch, "'ESC' To Close", (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, 30);

            if(Overlay.splineSelected > 0) {
                font.setColor(Color.GOLDENROD);
                font.getData().setScale(0.7f);

                layout.setText(font, Overlay.splineManager.getSplineByID(Overlay.splineSelected).getName());
                font.draw(batch, Overlay.splineManager.getSplineByID(Overlay.splineSelected).getName(), (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 80);

                font.getData().setScale(0.6f);
                layout.setText(font, "ID: " + Overlay.splineManager.getSplineByID(Overlay.splineSelected).getID());
                font.draw(batch, "ID: " + Overlay.splineManager.getSplineByID(Overlay.splineSelected).getID(), (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 110);

            } else if(Overlay.waypointSelected > 0) {
                font.setColor(Color.GOLDENROD);
                font.getData().setScale(0.7f);

                layout.setText(font, "Waypoint");
                font.draw(batch, "Waypoint", (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 80);

                font.getData().setScale(0.6f);
                layout.setText(font, "ID: " + Overlay.waypointManager.getWaypointByID(Overlay.waypointSelected).getID());
                font.draw(batch, "ID: " + Overlay.waypointManager.getWaypointByID(Overlay.waypointSelected).getID(), (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 110);

            }
        } else {
            font.setColor(Color.SALMON);
            layout.setText(font, "Properties");
            if(currentWidth.dst(new Vector2(0, 0)) > 185) {
                font.draw(batch, "Properties", (Gdx.graphics.getWidth() - currentWidth.x + (currentWidth.x / 2)) - layout.width / 2, Gdx.graphics.getHeight() - 40);
            }
        }

        batch.end();
    }

    public void show() {
        if(hidden) {
            hidden = false;

            currentX = new Vector2(Gdx.graphics.getWidth(), 0);
            target = new Vector2(Gdx.graphics.getWidth() - 250, 0);
            currentWidth = new Vector2(0, 0);
        }

    }

    public void hide() {
        if(!hidden) {
            hidden = true;

            currentX = new Vector2(Gdx.graphics.getWidth() - 250, 0);
            target = new Vector2(Gdx.graphics.getWidth(), 0);
            currentWidth = new Vector2(250, 0);
        }

    }
}
