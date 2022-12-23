package com.andedit.arcubit.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class BlockPos {
	public int x, y, z;
	
	public BlockPos() {
		
	}
	
	public BlockPos(BlockPos pos) {
		set(pos);
	}
	
	public BlockPos(int x, int y, int z) {
		set(x, y, z);
	}
	
	public BlockPos set(BlockPos pos) {
		return set(pos.x, pos.y, pos.z);
	}
	
	public BlockPos set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public BlockPos set(Vector3 pos) {
		return set(MathUtils.floor(pos.x), MathUtils.floor(pos.y), MathUtils.floor(pos.z));
	}
	
	/** set rounded */
	public BlockPos setR(Vector3 pos) {
		return set(MathUtils.round(pos.x), MathUtils.round(pos.y), MathUtils.round(pos.z));
	}
	
	public BlockPos add(BlockPos pos) {
		return add(pos.x, pos.y, pos.z);
	}
	
	public BlockPos add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public BlockPos sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	public BlockPos set(BlockPos pos, int x, int y, int z) {
		return set(pos.x+x, pos.y+y, pos.x+z);
	}
	
	/** Create a new BlockPos with a offset. */
	public BlockPos offset(Facing face) {
		return offset(face, 1);
	}
	
	/** Create a new BlockPos with a offset. */
	public BlockPos offset(Facing face, int n) {
		final BlockPos pos = face.offset;
		return offset(pos.x*n, pos.y*n, pos.z*n);
	}
	
	/** Create a new BlockPos with a offset. */
	public BlockPos offset(BlockPos pos) {
		return new BlockPos(this).add(pos);
	}
	
	/** Create a new BlockPos with a offset. */
	public BlockPos offset(int x, int y, int z) {
		return new BlockPos(this.x+x, this.y+y, this.z+z);
	}
	
	public Vector3 toVec() {
		return new Vector3(x, y, z);
	}
	
	@Override
	public int hashCode() {
		return 29 * z + 1721 * x + 95713 * y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof BlockPos p) {
			return p.x == x && p.y == y && p.z == z;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
	
	@Override
	public BlockPos clone() {
		return new BlockPos(this);
	}
	
	public static class Immutable extends BlockPos {
		
		public Immutable(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		/** Rounded BlockPos, not floored. */
		public Immutable(Vector3 vec) {
			this.x = MathUtils.round(vec.x);
			this.y = MathUtils.round(vec.y);
			this.z = MathUtils.round(vec.z);
		}

		@Override
		public BlockPos set(BlockPos pos, int x, int y, int z) {
			throw new UnsupportedOperationException("immutable");
		}
		
		@Override
		public BlockPos add(int x, int y, int z) {
			throw new UnsupportedOperationException("immutable");
		}
		
		@Override
		public BlockPos sub(int x, int y, int z) {
			throw new UnsupportedOperationException("immutable");
		}
	}
}
