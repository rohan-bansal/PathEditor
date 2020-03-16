package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Button {

    private BitmapFont font;
    private GlyphLayout layout;
    private Sprite sprite, sprite_highlighted;
    private String name;
    private int function;

    public boolean hovered = false;

    public Button(String name) {

        this.name = name;

        sprite = new Sprite(new Texture(Gdx.files.internal("frames/box.png")));
        sprite_highlighted = new Sprite(new Texture(Gdx.files.internal("frames/box_highlighted.png")));

        this.font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
        this.font.getData().setScale(0.7f);

        layout = new GlyphLayout();
    }

    public void update(SpriteBatch batch, CameraController cam) {

        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.getCamera().unproject(vec);

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        if(sprite.getBoundingRectangle().contains(vec.x, vec.y)) {
            sprite_highlighted.draw(batch);
            hovered = true;
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                ButtonFunctions.process(this.function);
            }
        } else {
            sprite.draw(batch);
            hovered = false;
        }

        String name;
        String[] split = this.name.split(" ");
        if(split.length > 1) {
            name = split[0].charAt(0) + "" + split[1].charAt(0);
        } else {
            name = split[0].charAt(0) + "";
        }
        layout.setText(font, name);
        font.draw(batch, name, (sprite.getX() + (float) sprite.getWidth() / 2) - layout.width / 2, sprite.getY() + 30);

        batch.end();
    }

    public Button setCenter(float x, float y) {
        this.sprite.setCenter(x, y);
        this.sprite_highlighted.setCenter(x, y);

        return this;
    }

    public Button setFunction(int function) {
        this.function = function;

        return this;
    }

    public String getName() {
        return name;
    }
}
