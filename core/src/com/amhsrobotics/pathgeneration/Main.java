package com.amhsrobotics.pathgeneration;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.cameramechanics.InputCore;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;

public class Main extends ApplicationAdapter {

	private CameraController worldCam;
	private CameraController hudCam;
	private Overlay overlay;
	private SpriteBatch batch;

	public static Stage stage;

	@Override
	public void create () {

		stage = new Stage();
		stage.getRoot().addCaptureListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
				return false;
			}});

		batch = new SpriteBatch();

		worldCam = new CameraController(false);
		hudCam = new CameraController(true);

		overlay = new Overlay();

		Gdx.input.setInputProcessor(new InputMultiplexer(new InputCore(worldCam), stage));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(90 / 255f, 74 / 255f, 70 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldCam.update();
		hudCam.update();

		overlay.updateAll(batch, worldCam, hudCam);

		stage.act(Gdx.graphics.getDeltaTime());
		//stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
