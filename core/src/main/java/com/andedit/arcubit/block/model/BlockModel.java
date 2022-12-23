package com.andedit.arcubit.block.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.andedit.arcubit.block.Block;
import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.block.model.BlockModel.Quad;
import com.andedit.arcubit.graphic.mesh.MeshBuilder;
import com.andedit.arcubit.graphic.mesh.MeshConsumer;
import com.andedit.arcubit.util.BlockPos;
import com.andedit.arcubit.util.Facing;
import com.andedit.arcubit.util.Facing.Axis;
import com.andedit.arcubit.util.MatrixStack;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Null;

// v3-----v2
// |       |
// |       |
// v4-----v1
public class BlockModel implements Iterable<Quad>, BlockRender {
	
	/** tolerance */
	private static final float T = 0.001f;
	
	private final ArrayList<Quad> quads = new ArrayList<>();
	private final ArrayList<BoundingBox> boxes = new ArrayList<>();
	
	/** ambient occlusion */
	public boolean ao = true;
	
	public BlockModel() {
		
	}
	
	@Override
	public void build(MeshBuilder consumer, World world, BlockPos pos) {
		for (int i=0,s=quads.size(); i < s; i++) {
			quads.get(i).build(consumer, world, pos);
		}
	}

	@Override
	public void build(MeshConsumer consumer, MatrixStack stack) {
		for (int i=0,s=quads.size(); i < s; i++) {
			quads.get(i).build(consumer, stack);
		}
	}
	
	@Override
	public void getQuads(List<Quad> list) {
		for (int i=0,s=quads.size(); i < s; i++) {
			list.add(quads.get(i));
		}
	}
	
	@Override
	public void getBoxes(List<BoundingBox> list) {
		for (int i=0,s=boxes.size(); i < s; i++) {
			list.add(boxes.get(i));
		}
	}
	
	/** a = from, b = to. 0 to 16 */
	public Cube cube(float Ax, float Ay, float Az, float Bx, float By, float Bz) {
		var box = new BoundingBox(new Vector3(Ax, Ay, Az).scl(1/16f), new Vector3(Bx, By, Bz).scl(1/16f));
		
		Quad up = newQuad(box);
		box.getCorner111(up.v1);
		box.getCorner110(up.v2);
		box.getCorner010(up.v3);
		box.getCorner011(up.v4);
		
		Quad down = newQuad(box);
		box.getCorner100(down.v1);
		box.getCorner101(down.v2);
		box.getCorner001(down.v3);
		box.getCorner000(down.v4);
		
		Quad north = newQuad(box);
		box.getCorner000(north.v1);
		box.getCorner010(north.v2);
		box.getCorner110(north.v3);
		box.getCorner100(north.v4);
		
		Quad east = newQuad(box);
		box.getCorner100(east.v1);
		box.getCorner110(east.v2);
		box.getCorner111(east.v3);
		box.getCorner101(east.v4);
		
		Quad south = newQuad(box);
		box.getCorner101(south.v1);
		box.getCorner111(south.v2);
		box.getCorner011(south.v3);
		box.getCorner001(south.v4);
		
		Quad west = newQuad(box);
		box.getCorner001(west.v1);
		box.getCorner011(west.v2);
		box.getCorner010(west.v3);
		box.getCorner000(west.v4);
		
		boxes.add(box);
		return new Cube(box, up, down, north, east, south, west);
	}
	
	public Quad newQuad() {
		return newQuad(null);
	}
	
	private Quad newQuad(@Null BoundingBox box) {
		var quad = new Quad(box);
		quads.add(quad);
		return quad;
	}
	
	/** no ambient occlusion */
	public BlockModel noAO() {
		ao = false;
		return this;
	}
	
	@Override
	public Iterator<Quad> iterator() {
		return quads.iterator();
	}
	
	public class Quad implements BlockRender {
		public final Vector3 v1 = new Vector3();
		public final Vector3 v2 = new Vector3();
		public final Vector3 v3 = new Vector3();
		public final Vector3 v4 = new Vector3();
		
		public final Vector2 t1 = new Vector2();
		public final Vector2 t2 = new Vector2();
		public final Vector2 t3 = new Vector2();
		public final Vector2 t4 = new Vector2();
		
		@Null
		public Facing face;
		public boolean shade = true;
		
		@Null
		public final BoundingBox box;
		private boolean borderCollide;
		
		private Quad(BoundingBox box) {
			this.box = box;
		}
		
		@Override
		public void build(MeshBuilder consumer, World world, BlockPos pos) {
			List<Quad> quads = consumer.quads;
			quads.clear();
			if (borderCollide) {
				var off = pos.offset(face);
				if (World.isOutBound(off)) {
					return;
				}
				world.getBlock(off).getRender().getQuads(quads);
			}
			
			if (canRender(quads)) {
				consumer.setLight(BlockRender.getShade(face));
				float x = pos.x, y = pos.y, z = pos.z;
				consumer.vert(v1.x+x, v1.y+y, v1.z+z, t1.x, t1.y);
				consumer.vert(v2.x+x, v2.y+y, v2.z+z, t2.x, t2.y);
				consumer.vert(v3.x+x, v3.y+y, v3.z+z, t3.x, t3.y);
				consumer.vert(v4.x+x, v4.y+y, v4.z+z, t4.x, t4.y);
			}
		}

		@Override
		public void build(MeshConsumer consumer, MatrixStack stack) {
			consumer.vert(stack.mulIn(v1), t1);
			consumer.vert(stack.mulIn(v2), t2);
			consumer.vert(stack.mulIn(v3), t1);
			consumer.vert(stack.mulIn(v4), t1);
		}
		
		@Override
		public void getQuads(List<Quad> list) {
			list.add(this);
		}
		
		@Override
		public void getBoxes(List<BoundingBox> list) {
			if (box != null) 
			list.add(box);
		}
		
		/** Test for whether this quad is'nt blocked by the quads. */
		boolean canRender(Block block) {
			return block == Blocks.AIR;
		}
		
		/** Test for whether this quad is'nt blocked by the quads. */
		boolean canRender(List<Quad> quads) {
			var boxA = box;
			float areaCovered = 0;
			for (int i=0,s=quads.size(); i < s; i++) {
				var quad = quads.get(i);
				if (!quad.borderCollide && face == quad.face.invert()) {
					continue;
				}
				
				var boxB = quad.box;
				final float uMinA, uMaxA, uMinB, uMaxB;
				final float vMinA, vMaxA, vMinB, vMaxB;
				if (face.axis == Axis.Y) {
					uMinA = boxA.min.x;
					uMaxA = boxA.max.x;
					uMinB = boxB.min.x;
					uMaxB = boxB.max.x;
					vMinA = boxA.min.z;
					vMaxA = boxA.max.z;
					vMinB = boxB.min.z;
					vMaxB = boxB.max.z;
				} else {
					var axis = face.axis.right();
					uMinA = axis.getAxis(boxA.min);
					uMaxA = axis.getAxis(boxA.max);
					uMinB = axis.getAxis(boxB.min);
					uMaxB = axis.getAxis(boxB.max);
					vMinA = boxA.min.y;
					vMaxA = boxA.max.y;
					vMinB = boxB.min.y;
					vMaxB = boxB.max.y;
				}
				
				if (uMinA < uMaxB && uMaxA > uMinB && vMinA < vMaxB && vMaxA > vMinB) {
					float cWid = Math.min(uMaxA, uMaxB) - Math.max(uMinA, uMinB);
					float cHei = Math.min(vMaxA, vMaxB) - Math.max(vMinA, vMinB);
					float aWid = uMaxA - uMinA;
					float aHei = vMaxA - vMinA;
					areaCovered += (cWid * cHei) / (aWid * aHei);
					if (areaCovered > 1f-T) return false;
				}
			}
			return true;
		}
		
		/** Set the face first for a proper uv. */
		public Quad reg(TextureRegion region) {
			if (face == null) {
				t1.set(region.getU2(), region.getV2());
				t2.set(region.getU2(), region.getV());
				t3.set(region.getU(), region.getV());
				t4.set(region.getU(), region.getV2());
				return this;
			}
			
			float 
			cu1, cv1,
			cu2, cv2,
			cu3, cv3,
			cu4, cv4;
			
			switch (face) {
			case UP:
				cu1 = v1.x; cv1 = v1.z;
				cu2 = v2.x; cv2 = v2.z;
				cu3 = v3.x; cv3 = v3.z;
				cu4 = v4.x; cv4 = v4.z;
				break;
			case DOWN:
				cu1 = 1f-v1.x; cv1 = 1f-v1.z;
				cu2 = 1f-v2.x; cv2 = 1f-v2.z;
				cu3 = 1f-v3.x; cv3 = 1f-v3.z;
				cu4 = 1f-v4.x; cv4 = 1f-v4.z;
				break;
			case NORTH:
				cu1 = 1f-v1.x; cv1 = 1f-v1.y;
				cu2 = 1f-v2.x; cv2 = 1f-v2.y;
				cu3 = 1f-v3.x; cv3 = 1f-v3.y;
				cu4 = 1f-v4.x; cv4 = 1f-v4.y;
				break;
			case EAST:
				cu1 = 1f-v1.z; cv1 = 1f-v1.y;
				cu2 = 1f-v2.z; cv2 = 1f-v2.y;
				cu3 = 1f-v3.z; cv3 = 1f-v3.y;
				cu4 = 1f-v4.z; cv4 = 1f-v4.y;
				break;
			case SOUTH:
				cu1 = v1.x; cv1 = 1f-v1.y;
				cu2 = v2.x; cv2 = 1f-v2.y;
				cu3 = v3.x; cv3 = 1f-v3.y;
				cu4 = v4.x; cv4 = 1f-v4.y;
				break;
			case WEST:
				cu1 = v1.z; cv1 = 1f-v1.y;
				cu2 = v2.z; cv2 = 1f-v2.y;
				cu3 = v3.z; cv3 = 1f-v3.y;
				cu4 = v4.z; cv4 = 1f-v4.y;
				break;
			default:
				cu1 = 1; cv1 = 1;
				cu2 = 1; cv2 = 1;
				cu3 = 1; cv3 = 1;
				cu4 = 1; cv4 = 1;
			}
			
			final float
			uOffset = region.getU(),
			vOffset = region.getV(),
			uScale = region.getU2() - uOffset,
			vScale = region.getV2() - vOffset;
			
			t1.set(uOffset + uScale * cu1, vOffset + vScale * cv1);
			t2.set(uOffset + uScale * cu2, vOffset + vScale * cv2);
			t3.set(uOffset + uScale * cu3, vOffset + vScale * cv3);
			t4.set(uOffset + uScale * cu4, vOffset + vScale * cv4);
			
			return this;
		}
		
		/** Set face. Will disable shade if set to face with null. */
		public Quad face(@Null Facing face) {
			if (this.face == face) return this; 
			this.face = face;
			if (face == null) {
				borderCollide = false;
				return noShade();
			}
			
			if (box != null) {
				borderCollide = MathUtils.isEqual(Math.abs(face.getAxis(box)-0.5f), 0.5f, T);  
			}
			
			return this;
		}
		
		public Quad noShade() {
			shade = false;
			return this;
		}
	}
	
	public class Cube implements Iterable<Quad> {
		public final BoundingBox box;
		public final Quad up;
		public final Quad down;
		public final Quad north;
		public final Quad east;
		public final Quad south;
		public final Quad west;
		
		private Cube(BoundingBox box, Quad up, Quad down, Quad north, Quad east, Quad south, Quad west) {
			this.box = box;
			this.up = up.face(Facing.UP);
			this.down = down.face(Facing.DOWN);
			this.north = north.face(Facing.NORTH);
			this.east = east.face(Facing.EAST);
			this.south = south.face(Facing.SOUTH);
			this.west = west.face(Facing.WEST);
		}
		
		public Cube regAll(TextureRegion region) {
			forEach(q->q.reg(region));
			return this;
		}
		
		public Cube regSide(TextureRegion region) {
			north.reg(region);
			east.reg(region);
			south.reg(region);
			west.reg(region);
			return this;
		}
		
		/** vertical (up and down) */
		public Cube regVert(TextureRegion region) {
			up.reg(region);
			down.reg(region);
			return this;
		}
		
		public Cube removeExcept(Facing... faces) {
			quads.removeIf(q -> {
				for (var face : faces) {
					if (face == q.face) {
						return false;
					}
				}
				return true;
			});
			return this;
		}
		
		public Cube remove(Facing... faces) {
			quads.removeIf(q -> {
				for (var face : faces) {
					if (face == q.face) {
						return true;
					}
				}
				return false;
			});
			return this;
		}
		
		public Cube remove(Facing face) {
			quads.remove(get(face));
			return this;
		}
		
		public Quad get(Facing face) {
			return switch (face) {
				case UP -> up;
				case DOWN -> down;
				case NORTH -> north;
				case EAST -> east;
				case SOUTH -> south;
				case WEST -> west;
				default -> null;
			};
		}

		@Override
		public Iterator<Quad> iterator() {
			return new Iterator<Quad>() {
				final Iterator<Facing> faces = Facing.allIter();

				@Override
				public boolean hasNext() {
					return faces.hasNext();
				}

				@Override
				public Quad next() {
					return get(faces.next());
				}
			};
		}
	}
}
