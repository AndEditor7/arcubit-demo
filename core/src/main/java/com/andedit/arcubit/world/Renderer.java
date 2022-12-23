package com.andedit.arcubit.world;

import static com.andedit.arcubit.graphic.MeshVert.shader;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.math.MathUtils.floor;

import java.util.Arrays;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.graphic.Camera;
import com.andedit.arcubit.graphic.QuadIndex;
import com.andedit.arcubit.graphic.mesh.MeshBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;

/** The world renderer */
public class Renderer implements Disposable {
	private static final int SIZE = World.SIZE>>4, HEIGHT = World.HEIGHT>>4;
	static final int RADIUS = 8;
	
	private final Array<Chunk> chunks = new Array<>(100);
	private final GridPoint3 chunkPos = new GridPoint3();
	private final boolean[] dirts = new boolean[SIZE * HEIGHT * SIZE];
	private final MeshBuilder consumer = new MeshBuilder();
	private World world;
	
	public Renderer() {
		// TODO Auto-generated constructor stub
	}
	
	public void setWorld(World world) {
		this.world = world;
		Arrays.fill(dirts, true);
	}
	
	public void render(Camera camera) {
		final Vector3 camPos = camera.position;
		chunkPos.set(floor(camPos.x)>>4, floor(camPos.y)>>4, floor(camPos.z)>>4);
		
		loop :
		for (int i = 0; i < RADIUS; i++)
		for (int x = chunkPos.x-i; x <= chunkPos.x+i; x++)
		for (int y = chunkPos.y-i; y <= chunkPos.y+i; y++)
		for (int z = chunkPos.z-i; z <= chunkPos.z+i; z++) {
			if (isOutBound(x, y, z)) continue;
			
			if (isDirty(x, y, z)) {
				setDirt(false, x, y, z);
				var chunk = getChunk(x, y, z);
				if (chunk == null) {
					chunk = new Chunk(this, x, y, z);
					chunks.add(chunk);
				}
				if (!chunk.build(world, consumer)) {
					break loop;
				}
			}
		}
		
		gl.glEnable(GL20.GL_CULL_FACE);
		Assets.meshTexture.bind();
		QuadIndex.preBind();
		shader.bind();
		shader.setUniformMatrix("u_projTrans", camera.combined);
		var planes = camera.getPlanes();
		for (int i = 0; i < chunks.size; i++) {
			var chunk = chunks.get(i);
			
			if (chunk.pass(chunkPos, 1) || chunk.isEmpty()) {
				chunks.removeIndex(i--);
				chunk.dispose();
				continue;
			}
			
			if (!chunk.pass(chunkPos, 0) && chunk.isVisible(planes)) {
				chunk.render();
			}
		}
		gl.glDisable(GL20.GL_CULL_FACE);
	}
	
	public static boolean isOutBound(int x, int y, int z) {
		return x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE;
	}
	
	public void dirty(int x, int y, int z) {
		int xAnd = x&15, yAnd = y&15, zAnd = z&15;
		x >>= 4; y >>= 4; z >>= 4;
		
		if (!isOutBound(x, y, z)) {
			setDirt(true, x, y, z);
		}
		
		if (xAnd == 0) {
			setDirt(true, x-1, y, z);
		} else if (xAnd == 15) {
			setDirt(true, x+1, y, z);
		}
		
		if (yAnd == 0) {
			setDirt(true, x, y-1, z);
		} else if (yAnd == 15) {
			setDirt(true, x, y+1, z);
		}
		
		if (zAnd == 0) {
			setDirt(true, x, y, z-1);
		} else if (zAnd == 15) {
			setDirt(true, x, y, z+1);
		}
	}
	
	@Null
	Chunk getChunk(int x, int y, int z) {
		for (var chunk : chunks) {
			if (chunk.equals((short)x, (short)y, (short)z)) {
				return chunk;
			}
		}
		return null;
	}
	
	void setDirt(boolean isDirty, int x, int y, int z) {
		if (isOutBound(x, y, z)) return;
		dirts[getIndex(x, y, z)] = isDirty;
	}
	
	boolean isDirty(int x, int y, int z) {
		return dirts[getIndex(x, y, z)];
	}
	
	public static int getIndex(int x, int y, int z) {
		return x + (y * SIZE) + (z * SIZE * HEIGHT);
	}

	@Override
	public void dispose() {
		chunks.forEach(Chunk::dispose);
	}
}
