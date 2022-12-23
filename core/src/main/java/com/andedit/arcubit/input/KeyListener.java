package com.andedit.arcubit.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class KeyListener extends InputAdapter {

	private final IntMap<Array<Runnable>> map = new IntMap<>();
	
	public KeyListener() {
		
	}

	public KeyListener(int key, Runnable runnable) {
		put(key, runnable);
	}
	
	public void put(int key, Runnable runnable) {
		Array<Runnable> array = map.get(key);
		if (array == null) {
			array = new Array<Runnable>(8);
			map.put(key, array);
		}
		array.add(runnable);
	}

	@Override
	public boolean keyDown(int keycode) {
		Array<Runnable> array = map.get(keycode);
		if (array != null) {
			array.forEach(Runnable::run);
			return true;
		}
		return false;
	}
}
