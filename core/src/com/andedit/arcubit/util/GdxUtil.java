package com.andedit.arcubit.util;

import com.andedit.arcubit.handle.Inputs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Disposable;

/** Basic libGDX utilities. TODO: move this to Util class. */
public class GdxUtil 
{
	/** Just look into this method... */
	public static void closeOnEsc()
	{
		if (Inputs.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	/** Creates the array list of disposable objects and release all resources. Also has null checking. */
	public static void disposes(Disposable... obj) {
		for (int i = 0, s = obj.length; i < s; i++) {
			if (obj[i] != null) obj[i].dispose();
		}
	}
}
