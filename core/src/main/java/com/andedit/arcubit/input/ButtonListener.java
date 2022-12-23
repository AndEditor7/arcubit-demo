package com.andedit.arcubit.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;

public class ButtonListener extends ControllerAdapter {
	
	private final IntArray keys = new IntArray();
	private final IntMap<Array<Runnable>> map = new IntMap<>();
	
	public ButtonListener() {
		
	}
	
	/** @see Codes for buttons */
	public ButtonListener(int utilCode, Runnable runnable) {
		put(utilCode, runnable);
	}
	
	/** @see Codes for buttons */
	public void put(int utilCode, Runnable runnable) {
		Array<Runnable> array = map.get(utilCode);
		if (array == null) {
			array = new Array<Runnable>(8);
			map.put(utilCode, array);
		}
		keys.add(utilCode);
		array.add(runnable);
	}

	@Override
	public boolean buttonDown(Controller control, int buttonIndex) {
		int key = -1;
		for (int i = 0; i < keys.size; i++) {
			if (Codes.toCode(control, keys.get(i)) == buttonIndex) {
				key = keys.get(i);
			}
		}
		Array<Runnable> array = map.get(key);
		if (array != null) {
			array.forEach(Runnable::run);
			return true;
		}
		return false;
	}
}
