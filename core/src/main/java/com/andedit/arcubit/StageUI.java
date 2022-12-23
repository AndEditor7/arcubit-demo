package com.andedit.arcubit;

import com.andedit.arcubit.graphic.FastBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** An extension of the Scene2d */
public class StageUI extends Stage {
	public StageUI() {
		super(new ScreenViewport(), new FastBatch());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		getBatch().dispose();
	}
}
