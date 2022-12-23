package com.andedit.arcubit.input.contoller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

public class DesktopController extends InputAdapter implements GameController {

	private final Vector2 move = new Vector2();
	private final Vector2 look = new Vector2();
	private final IntArray keysPressed = new IntArray(false, 16);
	private final GridPoint2 last = new GridPoint2();
	private final GridPoint2 delta = new GridPoint2();
	
	@Override
	public boolean keyDown (int keycode) {
		if (!keysPressed.contains(keycode)) {
			keysPressed.add(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		keysPressed.removeValue(keycode);
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		delta.x = last.x - screenX;
		last.x = screenX;
		delta.y = last.y - screenY;
		last.y = screenY;
		return false;
	}
	
	@Override
	public float getMoveY() {
		float move = 0;
		if (keysPressed.contains(Keys.SPACE)) {
			move += 1;
		} if (keysPressed.contains(Keys.SHIFT_LEFT)) {
			move -= 1;
		}
		return move;
	}

	@Override
	public Vector2 getMove() {
		move.setZero();
		if (keysPressed.contains(Keys.W)) {
			move.y += 1;
		} if (keysPressed.contains(Keys.S)) {
			move.y -= 1;
		} if (keysPressed.contains(Keys.D)) {
			move.x -= 1;
		} if (keysPressed.contains(Keys.A)) {
			move.x += 1;
		}
		return move.nor();
	}

	@Override
	public Vector2 getLook() {
		return look.set(delta.x * 0.3f, -delta.y * 0.3f);
	}

	@Override
	public boolean isUse() {
		return keysPressed.notEmpty() || Gdx.input.isTouched();
	}

	@Override
	public void reset() {
		keysPressed.clear();
		last.set(0, 0);
		clear();
	}

	@Override
	public void clear() {
		delta.set(0, 0);
	}
	
	@Override
	public InputProcessor getInput() {
		return this;
	}
}
