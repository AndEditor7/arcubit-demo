package com.andedit.arcubit.chunk.loader;

import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.loader.ChunkBuilderThreaded.VolatileMeshPacket;
import com.andedit.arcubit.util.VolatileFixedArray;
import com.andedit.arcubit.util.threads.AsyncThreaded;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.utils.async.AsyncResult;

public class MultiChunkLoader extends AsyncThreaded<VolatileFixedArray<VolatileMeshPacket>> 
{
	public final VolatileFixedArray<Chunk> chunks;
	private final VolatileFixedArray<VolatileMeshPacket> packets;
	private AsyncResult<VolatileFixedArray<VolatileMeshPacket>> result;
	public final int length;
	
	private final ChunkBuilderThreaded build;

	public MultiChunkLoader(World world, int length) {
		super("Chunk Loader");
		build = new ChunkBuilderThreaded(world);
		chunks = new VolatileFixedArray<Chunk>(length);
		packets = new VolatileFixedArray<VolatileMeshPacket>(length);
		for (int i = 0; i < length; i++) {
			packets.set(i, new VolatileMeshPacket());
		}
		this.length = length;
	}
	
	public void start() {
		result = exe.submit(this);
	}

	@Override
	public VolatileFixedArray<VolatileMeshPacket> call() throws Exception {
		//final long a = System.currentTimeMillis();
		final int size = chunks.size;
		for (int i = 0; i < size; i++) {
			final Chunk chunk = chunks.get(i);
			build.all(chunk, packets.get(i));
			chunk.isChunkSafe = true;
		}
		packets.size = size;
		//System.out.println(System.currentTimeMillis() - a);
		return packets;
	}

	@Override
	public VolatileFixedArray<VolatileMeshPacket> get() {
		if (result == null) return null;
		final VolatileFixedArray<VolatileMeshPacket> packet = result.get();
		result = null;
		return packet;
	}

	@Override
	public boolean isDone() {
		return result == null ? true : result.isDone();
	}

	@Override
	public void clear() {
		if (result != null && result.get() != null) {
			final int size = packets.size;
			for (int i = 0; i < size; i++) {
				packets.get(i).dispose();
			}
		}
		result = null;
	}
}
