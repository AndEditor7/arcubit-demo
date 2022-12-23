package com.andedit.arcubit;

import com.andedit.arcubit.input.ControlMultiplexer;
import com.andedit.arcubit.input.Inputs;
import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;

/** The base class of the game.  */
abstract class Base extends Game implements Disposable {

	public StageUI stage;
	public final InputMultiplexer inputs;
	public final ControlMultiplexer controls;
	
	protected Screen newScreen;
	
	/** Input lock for preventing in-game inputs like moving/looking around. */
	protected final ObjectSet<String> inputLocks;
	
	/** Whether the mouse cursor is catched. */
	private boolean isCatched;
	
	{
		inputs = new InputMultiplexer();
		controls = new ControlMultiplexer();
		inputLocks = new ObjectSet<>();
	}

	@Override
	public void render() {
		nextScreen();
		super.render();
		Gdx.gl.glUseProgram(0);
		
		stage.act();
		//stage.draw();
		Gdx.gl.glUseProgram(0);
		Inputs.reset();
	}

	protected void nextScreen() {
		if (newScreen == null)
			return;

		if (screen != null)
			screen.hide();
		
		screen = newScreen;
		newScreen = null;

		// Always clear things when switching screen.
		stage.clear(); 
		inputs.clear();
		controls.clear();
		inputLocks.clear();
		setCatched(false);
		
		screen.show();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		Inputs.clear();
	}

	@Override
	public void setScreen(Screen screen) {
		newScreen = screen;
	}
	
	/** Whether the mouse cursor is catched. */
	public boolean isCatched() {
		return isCatched;
	}
	
	public void setCatched(boolean isCatched) {
		Gdx.input.setCursorCatched(isCatched);
		this.isCatched = isCatched;
	}
	
	public void setCursorPos(boolean centor) {
		if (centor) {
			Gdx.input.setCursorPosition(Util.getW()>>1, Util.getH()>>1);
			//stage.setCrossPos(Main.WIDTH>>1, Main.HEIGHT>>1);
		} else {
			Gdx.input.setCursorPosition(0, 0);
		}
	}
	
	public void addInputLock(String key) {
		inputLocks.add(key);
	}
	
	public void removeInputLock(String key) {
		inputLocks.remove(key);
	}
	
	public boolean isInputLock() {
		return inputLocks.notEmpty();
	}

	@Override
	public void dispose() {
		if (screen != null) {
			screen.dispose();
			screen = null;
		}
			
		if (newScreen != null) {
			newScreen.dispose();
			newScreen = null;
		}
		
		stage.dispose();
	}
}
