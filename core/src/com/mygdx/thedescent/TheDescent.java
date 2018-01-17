package com.mygdx.thedescent;

import com.badlogic.gdx.Game;

public class TheDescent extends Game {

	@Override
	public void create() {
		this.setScreen(new GameScreen());
	}
}