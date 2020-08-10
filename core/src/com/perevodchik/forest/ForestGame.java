package com.perevodchik.forest;
import com.badlogic.gdx.Game;

public class ForestGame extends Game {

	@Override
	public void create () {
		ForestGameScreen screen = new ForestGameScreen();
		setScreen(screen);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
