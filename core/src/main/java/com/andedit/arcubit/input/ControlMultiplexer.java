package com.andedit.arcubit.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class ControlMultiplexer implements ControllerListener {
	private final SnapshotArray<ControllerListener> processors = new SnapshotArray<>(true, 4, ControllerListener.class);

	public ControlMultiplexer() {
		
	}

	public ControlMultiplexer(ControllerListener... processors) {
		this.processors.addAll(processors);
	}
	
	public void addProcessor(int index, ControllerListener processor) {
		if (processor == null) throw new NullPointerException("processor cannot be null");
		processors.insert(index, processor);
	}

	public void removeProcessor(int index) {
		processors.removeIndex(index);
	}

	public void addProcessor(ControllerListener processor) {
		if (processor == null) throw new NullPointerException("processor cannot be null");
		processors.add(processor);
	}

	public void removeProcessor(ControllerListener processor) {
		processors.removeValue(processor, true);
	}

	/** @return the number of processors in this multiplexer */
	public int size() {
		return processors.size;
	}

	public void clear() {
		processors.clear();
	}

	public void setProcessors(ControllerListener... processors) {
		this.processors.clear();
		this.processors.addAll(processors);
	}

	public void setProcessors(Array<ControllerListener> processors) {
		this.processors.clear();
		this.processors.addAll(processors);
	}

	public SnapshotArray<ControllerListener> getProcessors() {
		return processors;
	}
	
	@Override
	public void connected(Controller controller) {
		ControllerListener[] items = processors.begin();
		try {
			for (int i = 0; i < size(); i++)
				items[i].connected(controller);
		} finally {
			processors.end();
		}
	}

	@Override
	public void disconnected(Controller controller) {
		ControllerListener[] items = processors.begin();
		try {
			for (int i = 0; i < size(); i++)
				items[i].disconnected(controller);
		} finally {
			processors.end();
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		ControllerListener[] items = processors.begin();
		try {
			for (int i = 0; i < size(); i++)
				if (items[i].buttonDown(controller, buttonCode)) return true;
		} finally {
			processors.end();
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		ControllerListener[] items = processors.begin();
		try {
			for (int i = 0; i < size(); i++)
				if (items[i].buttonUp(controller, buttonCode)) return true;
		} finally {
			processors.end();
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		ControllerListener[] items = processors.begin();
		try {
			for (int i = 0; i < size(); i++)
				if (items[i].axisMoved(controller, axisCode, value)) return true;
		} finally {
			processors.end();
		}
		return false;
	}
}
