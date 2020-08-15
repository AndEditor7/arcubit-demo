package com.andedit.arcubit.block;

import com.andedit.arcubit.renderer.TexLib;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Block 
{
	public final byte id;
	public final String name;
	public final boolean isSoild;
	public final boolean isTrans;
	public final boolean collision;
	public final BlockType type;
	public BlockTex textures;
	
	public Block(byte id, String name, boolean isSoildNcollision, BlockType type) {
		this(id, name, isSoildNcollision, isSoildNcollision, type);
	}
	
	
	
	public Block(byte id, String name, boolean isSoild, boolean collision, BlockType type) {
		this(id, name, isSoild, !isSoild, collision, type);
	}
	
	public Block(byte id, String name, boolean isSoild, boolean isTrans, boolean collision, BlockType type) {
		this.id = id;
		this.name = name;
		this.isSoild = isSoild;
		this.isTrans = isTrans;
		this.collision = collision;
		this.type = type;
	}
	
	public Block tex(TextureRegion all) {
		textures = new BlockTex(all, all, all);
		return this;
	}
	
	public Block tex(TextureRegion topBottom, TextureRegion side) {
		textures = new BlockTex(topBottom, side, topBottom);
		return this;
	}
	
	public Block tex(TextureRegion top, TextureRegion side, TextureRegion topDown) {
		textures = new BlockTex(top, side, topDown);
		return this;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj.getClass() == Block.class) {
			return ((Block)obj).id == id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(32);
		build.append("id: ").append(id&0xFF).append('\n');
		build.append("name: ").append(name).append('\n');
		build.append("isSoild: ").append(isSoild).append('\n');
		build.append("collision: ").append(collision).append('\n');
		return build.toString();
	}
	
	public static final class BlockTex {
		public final TextureRegion top, side, bottom;
		
		public BlockTex(TextureRegion top, TextureRegion side, TextureRegion bottom) {
			this.top = top == null ? TexLib.missing : top;
			this.side = side == null ? TexLib.missing : side;
			this.bottom = bottom == null ? TexLib.missing : bottom;
		}
	}
}
