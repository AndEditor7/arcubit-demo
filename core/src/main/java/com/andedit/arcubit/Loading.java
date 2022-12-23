package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.input.Inputs;
import com.andedit.arcubit.util.AssetManager;
import com.badlogic.gdx.ScreenAdapter;

class Loading extends ScreenAdapter {
	
	private final AssetManager asset;
	
	Loading(AssetManager asset) {
		this.asset = asset;
		Assets.load(asset);
	}
	
	@Override
	public void render(float delta) {
		if (asset.update(10)) {
			Assets.get(asset);
			Inputs.clear();
			Statics.init();
			main.setScreen(new TheMenu());
		}
	}
}
