package com.andedit.arcubit.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.utils.Null;

public class ControlHolder implements ControllerListener {
	
	@Null
	private ControllerListener listener;
	
	public void set(@Null ControllerListener listener) {
		this.listener = listener;
	}
	
	public void clear() {
		set(null);
	}

	@Override
	public void connected(Controller controller) {
		if (listener != null) {
			listener.connected(controller);
		}
	}

	@Override
	public void disconnected(Controller controller) {
		if (listener != null) {
			listener.disconnected(controller);
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return listener == null ? false : listener.buttonDown(controller, buttonCode);
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return listener == null ? false : listener.buttonUp(controller, buttonCode);
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return listener == null ? false : listener.axisMoved(controller, axisCode, value);
	}
	
}
