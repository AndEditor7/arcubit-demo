package com.andedit.arcubit.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Null;

public class InputHolder implements InputProcessor {
	
	@Null
	private InputProcessor processor;
	
	public void set(@Null InputProcessor processor) {
		this.processor = processor;
	}
	
	public void clear() {
		set(null);
	}

	@Override
	public boolean keyDown(int keycode) {
		return processor == null ? false : processor.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		return processor == null ? false : processor.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return processor == null ? false : processor.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return processor == null ? false : processor.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return processor == null ? false : processor.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return processor == null ? false : processor.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return processor == null ? false : processor.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return processor == null ? false : processor.scrolled(amountX, amountY);
	}
}
