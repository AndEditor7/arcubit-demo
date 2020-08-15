package com.andedit.arcubit.world;

import com.andedit.arcubit.Options;
import com.andedit.arcubit.PartiBench;
import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkBuilder;
import com.andedit.arcubit.chunk.ChunkBuilder.MeshPacket;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.chunk.loader.MultiChunkLoader;
import com.andedit.arcubit.chunk.loader.ChunkBuilderThreaded.VolatileMeshPacket;
import com.andedit.arcubit.mesh.ChunkMesh;
import com.andedit.arcubit.renderer.Clouds;
import com.andedit.arcubit.renderer.Clouds3D;
import com.andedit.arcubit.renderer.TexLib;
import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.util.Shaders;
import com.andedit.arcubit.util.Util;
import com.andedit.arcubit.util.VolatileFixedArray;
import com.andedit.arcubit.util.math.FrustUtil;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;

public final class WorldRenderer implements Disposable
{	
	/** Index Buffer - enables to reuse the vertex to render the "quad" */
	public static IndexData indices;
	
	/** Render table. */
	private final Array<ChunkMesh> 
	terrain = new Array<ChunkMesh>(false, 32),
	plant	= new Array<ChunkMesh>(false, 32),
	water   = new Array<ChunkMesh>(false, 32);
	
	/** Chunk/Mesh builder. For the render thread. */
	private final ChunkBuilder build;
	
	/** Cloud renderer. */
	private final Clouds clouds;
	
	/** The world object. */
	private final World world;
	
	// Chunk loader.
	boolean forceCheck = false, forceDirty = false;
	private final Queue<Chunk> updateQueue = new Queue<Chunk>(64);
	private final Queue<Chunk> dirtyQueue  = new Queue<Chunk>(16);
	
	
	private final MultiChunkLoader loader;
	
	WorldRenderer(World world, int maxDis) {
		this.world = world;
		loader = new MultiChunkLoader(world, 8);
		build = new ChunkBuilder(world);
		setMaxDistance(maxDis);
		
		final int len = 98304;
		final short[] index = new short[len];
		for (int i = 0, v = 0; i < len; i += 6, v += 4) {
			index[i] = (short)v;
			index[i+1] = (short)(v+1);
			index[i+2] = (short)(v+2);
			index[i+3] = (short)(v+2);
			index[i+4] = (short)(v+3);
			index[i+5] = (short)v;
		}
		
		if (Options.VBO) {
			indices = new IndexBufferObject(true, len);
		} else {
			indices = new IndexArray(len);
		}
		indices.setIndices(index, 0, len);
		
		clouds = new Clouds3D();
	}
	
	private final GridPoint3 lastPos  = new GridPoint3();
	private final GridPoint3 chunkPos = new GridPoint3();
	
	void render(Camera cam) {
		if (PartiBench.isBench) {
			chunkPos.set(PartiBench.getChunkPos());
		} else {
			chunkPos.set(MathUtils.floor(cam.position.x)>>4, MathUtils.floor(cam.position.y)>>4, MathUtils.floor(cam.position.z)>>4);
		}
		
		
		final Plane[] planes = cam.frustum.planes;
		
		if (loader.isDone()) {
		//if (true) {
			if (Gdx.input.isKeyJustPressed(Keys.L)) {
				clear();
				loader.clear();
				dirtyQueue.clear();
				lastPos.set(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
				return;
			}
			
			// TODO: Try to optimize it.
			if (forceDirty) {
				forceDirty = false;
				dirtyQueue.clear();
				checkChunk(0);
			}
			
			if (dirtyQueue.notEmpty()) {
				handleChunk(build.create(dirtyQueue.removeFirst()));
			}
			
			final VolatileFixedArray<VolatileMeshPacket> packets = loader.get();
			if (packets != null) {
				final int size = packets.size;
				for (int i = 0; i < size; i++) {
					handleChunk(packets.get(i));
				}
			}
			
			if (lastPos.x != chunkPos.x || lastPos.z != chunkPos.z) {
				lastPos.set(chunkPos);
				for (Chunk chunk : updateQueue)
					chunk.isNewChunk = true;
				updateQueue.clear();
				checkNewChunk(0);
			}
			
			if (updateQueue.notEmpty()) {
				final VolatileFixedArray<Chunk> chunks = loader.chunks;
				chunks.clear();
				final int len = loader.length;
				while (updateQueue.notEmpty()) {
					final Chunk chunk = updateQueue.removeFirst();
					chunk.isChunkSafe = false;
					chunks.add(chunk);
					if (chunks.size == len) break;
				}
				loader.start();
			}
		}
		
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		if (!PartiBench.isBench) clouds.render(cam, indices);
		
		TexLib.missing.getTexture().bind();
		Shaders.bindTerrain(cam.combined);
		for (int i = 0; i < terrain.size; i++) {
			ChunkMesh mesh = terrain.get(i);
			Chunk chunk = mesh.chunk;
			if (chunk.x > chunkPos.x+renderMax || chunk.z > chunkPos.z+renderMax || 
				chunk.x < chunkPos.x-renderMax || chunk.z < chunkPos.z-renderMax) {
				mesh.dispose();
				chunk.setNewChunk(true);
				terrain.removeIndex(i--);
				continue;
			}
			if (FrustUtil.frustBounds(planes, chunk)) 
				mesh.render(indices);
		}
		if (Options.GL3) Gdx.gl30.glBindVertexArray(0);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Shaders.bindPlant(cam.combined);
		for (int i = 0; i < plant.size; i++) {
			ChunkMesh mesh = plant.get(i);
			Chunk chunk = mesh.chunk;
			if (chunk.x > chunkPos.x+renderMax || chunk.z > chunkPos.z+renderMax || 
				chunk.x < chunkPos.x-renderMax || chunk.z < chunkPos.z-renderMax) {
				mesh.dispose();
				chunk.setNewChunk(true);
				plant.removeIndex(i--);
				continue;
			}
			if (FrustUtil.frustBounds(planes, chunk)) 
				mesh.render(indices);
		}
		if (Options.GL3) Gdx.gl30.glBindVertexArray(0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Shaders.bindWater(cam.combined);
		for (int i = 0; i < water.size; i++) {
			ChunkMesh mesh = water.get(i);
			Chunk chunk = mesh.chunk;
			if (chunk.x > chunkPos.x+renderMax || chunk.z > chunkPos.z+renderMax || 
				chunk.x < chunkPos.x-renderMax || chunk.z < chunkPos.z-renderMax) {
				mesh.dispose();
				chunk.setNewChunk(true);
				water.removeIndex(i--);
				continue;
			}
			if (FrustUtil.frustBounds(planes, chunk)) 
				mesh.render(indices);
		}
		if (Options.GL3) Gdx.gl30.glBindVertexArray(0);
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	private int renderMaxSize;
	private int renderMax;
	
	// TODO: Use 2nd Arcubit's checkChunk.
	private void checkNewChunk(int renderSize)
	{
		for (int x = -renderSize; x < renderSize+1; x++)
		{
			for (int z = -renderSize; z < renderSize+1; z++)
			{
				ChunkRegion region = world.getChunkRegion(x+chunkPos.x, z+chunkPos.z);
				if (region == null) continue;
				for (int i = ChunkRegion.LENGTH-1; i > -1; i--)	{
					//if (handleChunk(region.chunks[i])) return;
					final Chunk chunk = region.chunks[i];
					if (chunk.isNewChunk) {
						chunk.isNewChunk = false;
						updateQueue.addLast(chunk);
					}
				}
			}
		}
		
		if (renderSize >= renderMaxSize)
			return;
		
		checkNewChunk(renderSize+1);		
	}
	
	private void checkChunk(int renderSize)
	{
		for (int x = -renderSize; x < renderSize+1; x++)
		{
			for (int z = -renderSize; z < renderSize+1; z++)
			{
				ChunkRegion region = world.getChunkRegion(x+chunkPos.x, z+chunkPos.z);
				if (region == null) continue;
				for (int i = ChunkRegion.LENGTH-1; i > -1; i--)	{
					final Chunk chunk = region.chunks[i];
					if (chunk.isDirty) {
						chunk.isDirty = false;
						dirtyQueue.addLast(chunk);
					}
				}
			}
		}
		
		if (renderSize >= renderMaxSize)
			return;
		
		checkChunk(renderSize+1);		
	}
	
	/*
	private final Queue<Chunk> chunkQue = new Queue<Chunk>();
	private void BFScheckChunks() {
		chunkQue.clear();
		final Chunk c = world.getChunk(chunkPos.x, chunkPos.y, chunkPos.z);
		if (c != null && c.isNewChunk) {
			chunkQue.addFirst(world.getChunk(chunkPos.x, chunkPos.y, chunkPos.z));
		}
		while(chunkQue.notEmpty()) {
			final Chunk chunk = chunkQue.removeFirst();
			updateQueue.addLast(chunk);
			
			Chunk newChunk;
			if ((newChunk = world.getChunk(chunk.x+1, chunk.y, chunk.z)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
			if ((newChunk = world.getChunk(chunk.x-1, chunk.y, chunk.z)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
			if ((newChunk = world.getChunk(chunk.x, chunk.y, chunk.z+1)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
			if ((newChunk = world.getChunk(chunk.x, chunk.y, chunk.z-1)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
			if ((newChunk = world.getChunk(chunk.x, chunk.y-1, chunk.z)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
			if ((newChunk = world.getChunk(chunk.x, chunk.y+1, chunk.z)) != null && newChunk.isNewChunk) {
				newChunk.isNewChunk = false;
				chunkQue.addLast(newChunk);
			}
		}
	} */
	
	void setMaxDistance(int chunks) {
		renderMaxSize = chunks;
		renderMax = chunks+1; // TODO: changed from 2 to 1
	}
	
	void clear() {
		for (int i = 0; i < terrain.size; i++) {
			ChunkMesh mesh = terrain.get(i);
			mesh.chunk.setNewChunk(true);
			mesh.dispose();
		}
		for (int i = 0; i < plant.size; i++) {
			ChunkMesh mesh = plant.get(i);
			mesh.chunk.setNewChunk(true);
			mesh.dispose();
		}
		for (int i = 0; i < water.size; i++) {
			ChunkMesh mesh = water.get(i);
			mesh.chunk.setNewChunk(true);
			mesh.dispose();
		}
		terrain.clear();
		plant.clear();
		water.clear();
	}

	@Override
	public void dispose() {
		int i, s;
		for (i = 0, s = terrain.size; i < s; i++) {
			terrain.get(i).dispose();
		}
		for (i = 0, s = plant.size; i < s; i++) {
			plant.get(i).dispose();
		}
		for (i = 0, s = water.size; i < s; i++) {
			water.get(i).dispose();
		}
		Util.disposes(clouds, loader, indices);
		indices = null;
	}
	
	private void handleChunk(VolatileMeshPacket packet) {
		if (packet == null) return;
		final Chunk chunk = packet.chunk;
		testMesh(chunk, packet.terrain, terrain);
		testMesh(chunk, packet.plant, plant);
		testMesh(chunk, packet.water, water);
	}
	
	private void handleChunk(MeshPacket packet) {
		if (packet == null) return;
		final Chunk chunk = packet.chunk;
		testMesh(chunk, packet.terrain, terrain);
		testMesh(chunk, packet.plant, plant);
		testMesh(chunk, packet.water, water);
	}
	
	private void testMesh(Chunk chunk, ChunkMesh newMesh, Array<ChunkMesh> meshs) {
		if (newMesh == null) {
			newMesh = findChunkMesh(meshs, chunk);
			if (newMesh != null) {
				newMesh.dispose();
				meshs.removeIndex(iLast);
			}
		} else {
			ChunkMesh mesh = findChunkMesh(meshs, chunk);
			if (mesh != null) {
				mesh.dispose();
				meshs.removeIndex(iLast);
			}
			meshs.add(newMesh);
		}
	}
	
	private int iLast;
	private ChunkMesh findChunkMesh(Array<ChunkMesh> meshs, Chunk chunk) {
		iLast = -1;
		final int size = meshs.size;
		for (int i = 0; i < size; i++) {
			ChunkMesh mesh = meshs.get(i);
			if (mesh.chunk == chunk) {
				iLast = i;
				return mesh;
			}
		}
		return null;
	}
}