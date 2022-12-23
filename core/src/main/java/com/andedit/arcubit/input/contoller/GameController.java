package com.andedit.arcubit.input.contoller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;

/** Game's controller interface. */
public interface GameController  {
	
	Vector2 getLook();
	
	Vector2 getMove();
	
	float getMoveY();
	
	@Null
	default InputProcessor getInput() {
		return null;
	};
	
	@Null
	default ControllerListener getController() {
		return null;
	};
	
	boolean isUse();
	
	/** Resets everything. */
	void reset();
	
	/** Resets just pressed input. */
	void clear();
}
