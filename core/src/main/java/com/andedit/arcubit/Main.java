package com.andedit.arcubit;

import static com.badlogic.gdx.Gdx.input;

import com.andedit.arcubit.graphic.MeshVert;
import com.andedit.arcubit.graphic.QuadIndex;
import com.andedit.arcubit.input.Inputs;
import com.andedit.arcubit.util.AssetManager;
import com.badlogic.gdx.InputMultiplexer;

/** The main class of the game.  */
public class Main extends Base 
{
	public static final Main main = new Main();
	private Main() {};
	
	private AssetManager asset;

	@Override
	public void create() {
		Config.init();
		QuadIndex.init();
		MeshVert.preInit();
		
		stage = new StageUI();
		input.setInputProcessor(new InputMultiplexer(stage, inputs, Inputs.input));
		
		asset = new AssetManager();
		setScreen(new Loading(asset));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		asset.dispose();
		Statics.dispose();
	}
}
