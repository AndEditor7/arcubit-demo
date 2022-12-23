package com.andedit.arcubit.util;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public enum Facing {
	/** Facing Y+ */
	UP  (-2, Axis.Y, pos(0, 1, 0)),
	/** Facing Y- */
	DOWN(-2, Axis.Y, pos(0, -1, 0)),
	/** Facing Z- */
	NORTH(0, Axis.Z, pos(0, 0, -1)),
	/** Facing X+ */
	EAST (1, Axis.X, pos(1, 0, 0)),
	/** Facing Z+ */
	SOUTH(2, Axis.Z, pos(0, 0, 1)), 
	/** Facing X- */
	WEST(-1, Axis.X, pos(-1, 0, 0));
	
	private final int num;
	
	public final Axis axis;
	public final BlockPos offset;
	
	Facing(int num, Axis axis, BlockPos offset) {
		this.num = num;
		this.offset = offset;
		this.axis = axis;
	}
	
	public Facing rotateRight() {
		switch (this) {
		case NORTH: return EAST;
		case EAST:  return SOUTH;
		case SOUTH: return WEST;
		case WEST:  return NORTH;
		default:    return this;
		}
	}
	
	public Facing rotateLeft() {
		switch (this) {
		case NORTH: return WEST;
		case EAST:  return NORTH;
		case SOUTH: return EAST;
		case WEST:  return SOUTH;
		default:    return this;
		}
	}
	
	public Facing rotate(int rotate) {
		if (num == -2) return this;
		switch ((num+rotate)&3) {
		case 0:  return NORTH;
		case 1:  return EAST;
		case 2:  return SOUTH;
		case 3:  return WEST;
		default: return this;
		}
	}
	
	public int getRotateValue() {
		return num;
	}
	
	public Facing invert() {
		return switch (this) {
		case UP    -> DOWN;
		case DOWN  -> UP;
		case NORTH -> SOUTH;
		case EAST  -> WEST;
		case SOUTH -> NORTH;
		case WEST  -> EAST;
		};
	}
	
	public boolean isPositive() {
		return this == UP || this == EAST || this == SOUTH;
	}
	
	private static final Facing[] ARRAY = values();
	public static final int SIZE = ARRAY.length;
	public static Facing get(int ordinal) {
		return ARRAY[MathUtils.clamp(ordinal, 0, 5)];
	}
	
	public float getAxis(BoundingBox box) {
		return axis.getAxis(isPositive() ? box.max : box.min);
	}
	
	public static enum Axis {
		X, Y, Z;
		
		public boolean isSide() {
			return this != Axis.Y;
		}
		
		public Axis right() {
			return switch (this) {
			case X, Y -> Z;
			case Z -> X;
			};
		}
		
		public float getAxis(Vector3 vec) {
			return switch (this) {
			case X -> vec.x;
			case Y -> vec.y;
			case Z -> vec.z;
			};
		}
		
		public int getAxis(BlockPos pos) {
			return switch (this) {
			case X -> pos.x;
			case Y -> pos.y;
			case Z -> pos.z;
			};
		}
	}
	
	public static Iterator<Facing> allIter() {
		return iter(0);
	}
	
	public static Iterator<Facing> sideIter() {
		return iter(2);
	}
	
	private static Iterator<Facing> iter(int start) {
		return new Iterator<Facing>() {
			int i = start;
			@Override
			public boolean hasNext() {
				return i < SIZE;
			}

			@Override
			public Facing next() {
				return ARRAY[i++];
			}
		};
	}
	
	private static BlockPos pos(int x, int y, int z) {
		return new BlockPos.Immutable(x, y, z); 
	}
}
