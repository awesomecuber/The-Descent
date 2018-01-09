package com.mygdx.thedescent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.mygdx.thedescent.GameScreen;

public class TheDescent extends Game {

	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen());
	}
}