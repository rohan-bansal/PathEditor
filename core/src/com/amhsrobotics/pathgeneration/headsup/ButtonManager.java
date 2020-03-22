package com.amhsrobotics.pathgeneration.headsup;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ButtonManager {

    private ArrayList<Button> buttons;

    public ButtonManager() {

        buttons = new ArrayList<Button>() {{
            add(new Button("Export").setFunction(ButtonFunctions.EXPORT).setCenter(40, Gdx.graphics.getHeight() / 2 + 300));
            add(new Button("Quintic Hermite").setFunction(ButtonFunctions.QUINTIC_HERMITE).setCenter(40, Gdx.graphics.getHeight() / 2 - 50));
            add(new Button("Cubic Hermite").setFunction(ButtonFunctions.CUBIC_HERMITE).setCenter(40, Gdx.graphics.getHeight() / 2 + 10));
            add(new Button("Find Point").setFunction(ButtonFunctions.FIND_POINT).setCenter(40, Gdx.graphics.getHeight() / 2 - 200));
            add(new Button("Waypoint").setFunction(ButtonFunctions.WAYPOINT).setCenter(40, Gdx.graphics.getHeight() / 2 - 140));
            add(new Button("Measure").setFunction(ButtonFunctions.MEASURE).setCenter(40, Gdx.graphics.getHeight() / 2 - 260));
        }};
    }

    public void updateAll(SpriteBatch batch, CameraController cam) {
        for(Button b : buttons) {
            b.update(batch, cam);
        }
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }
}
