package com.andedit.arcubit.block;

import com.andedit.arcubit.block.model.AirRender;
import com.andedit.arcubit.block.model.BlockRender;

public class Block {
	protected boolean hasCollision;
	protected BlockRender render = AirRender.INSTANCE;
	protected int id;
	
	public Block() {
		this(new Settings());
	}
	
	public Block(Settings settings) {
		hasCollision = settings.hasCollision;
	}
	
	public boolean isAir() {
		return false;
	}
	
	public BlockRender getRender() {
		return render;
	}
	
	public int getId() {
		return id;
	}
	
	public static Settings newSettings() {
		return new Settings();
	}

	public static class Settings {
		boolean hasCollision = true;
		
		public Settings noCollision() {
			hasCollision = false;
			return this;
		}
		
		public static Settings of() {
			return new Settings();
		}
	}
}
