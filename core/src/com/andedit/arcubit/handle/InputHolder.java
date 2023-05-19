package com.andedit.arcubit.handle;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

/** The InputHolder the holds inputs. */
public final class InputHolder extends InputAdapter
{
	public static InputHolder holder;
	
	private final Array<InputProcessor> processors = new Array<InputProcessor>();
	
	public InputHolder() {
		holder = this;
	}
	
	/** Add processor in low priority. */
	public void add(InputProcessor processor) {
		processors.add(processor);
	}
	
	/** Remove processor from the InputHolder. */
	public void remove(InputProcessor processor) {
		processors.removeValue(processor, true);
	}
	
	/** Removes all processors from this InputHolder. */
	public void clear() {
		processors.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.keyDown(keycode)) return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.keyUp(keycode)) return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.keyTyped(character)) return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.touchDown(screenX, screenY, pointer, button)) return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.touchUp(screenX, screenY, pointer, button)) return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.touchDragged(screenX, screenY, pointer)) return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.mouseMoved(screenX, screenY)) return true;
		}
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (processors.isEmpty()) return false;
		for (InputProcessor processor : processors) {
			if (processor.scrolled(amountX, amountY)) return true;
		}
		return false;
	}
}
