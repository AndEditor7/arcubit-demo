package com.andedit.arcubit.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexLib 
{	
	public static TextureRegion missing;
	public static TextureRegion cross;
	public static TextureAtlas atlas;
	
	
	public static void loadTexture()
	{
		atlas = new TextureAtlas(Gdx.files.internal("texture/blocks.atlas"));
		
		missing = atlas.findRegion("missing");
		cross = atlas.findRegion("cross");
	}
	
	public static TextureRegion getTex(final String name) {
		return atlas.findRegion(name);
	}
}
