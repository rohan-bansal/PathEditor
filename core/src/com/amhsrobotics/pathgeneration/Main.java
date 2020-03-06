package com.amhsrobotics.pathgeneration;

import com.amhsrobotics.pathgeneration.cameramechanics.CameraController;
import com.amhsrobotics.pathgeneration.cameramechanics.InputCore;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {

	private CameraController worldCam;
	private CameraController hudCam;
	private Overlay overlay;
	private SpriteBatch batch;
	
	@Override
	public void create () {

		batch = new SpriteBatch();

		worldCam = new CameraController(false);
		hudCam = new CameraController(true);

		overlay = new Overlay();

		Gdx.input.setInputProcessor(new InputCore(worldCam));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(90 / 255f, 74 / 255f, 70 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldCam.update();
		hudCam.update();

		overlay.updateAll(batch, worldCam, hudCam);

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
