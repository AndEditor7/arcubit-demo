package com.andedit.arcubit.util;

import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class BlockPos extends GridPoint3 
{
	private static final long serialVersionUID = -2585562597222979436L;

	public BlockPos() {
	}

	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPos(BlockPos pos) {
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
	}
	
	public BlockPos(Vector3 pos) {
		this.x = MathUtils.floor(x);
		this.y = MathUtils.floor(y);
		this.z = MathUtils.floor(z);
	}
	
	public BlockPos set(GridPoint3 pos) {
		super.set(pos);
		return this;
	}
	
	public BlockPos set(int x, int y, int z) {
		super.set(x, y, z);
		return this;
	}

	public BlockPos add(GridPoint3 other) {
		super.add(other);
		return this;
	}

	public BlockPos add(int x, int y, int z) {
		super.add(x, y, z);
		return this;
	}

	public BlockPos sub(GridPoint3 other) {
		super.sub(other);
		return this;
	}

	public BlockPos sub(int x, int y, int z) {
		super.sub(x, y, z);
		return this;
	}

	public BlockPos cpy() {
		return new BlockPos(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof GridPoint3) {
			GridPoint3 p = (GridPoint3)obj;
			return p.x == x && p.y == y && p.z == z;
		}
		return false;
	}
}
