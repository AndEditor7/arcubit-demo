package com.andedit.arcubit;

import com.andedit.arcubit.block.Renders;
import com.andedit.arcubit.graphic.MeshVert;
import com.andedit.arcubit.util.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public static Texture meshTexture;
	public static Texture guiTexture;
	public static BitmapFont font;
	public static final Skin skin = new Skin();

	static void load(AssetManager asset) {
		var resolver = asset.getFileHandleResolver();
		asset.setLoader(Texture.class, new TextureLoader(resolver));
		asset.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
		asset.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
		
		asset.load("textures/mozart.fnt", BitmapFont.class, t -> font = t);
		asset.load("textures/texture.png", Texture.class, t -> meshTexture = t);
		asset.load("shaders/mesh.vert", ShaderProgram.class, MeshVert::init);
	}
	
	static void get(AssetManager asset) {
		asset.getAll();
		new Renders(meshTexture);
		skin();
	}
	
	private static void skin() {
		
	}
}
