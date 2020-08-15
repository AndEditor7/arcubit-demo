package com.andedit.arcubit.particles;

import com.andedit.arcubit.particles.bits.Fragment;
import com.andedit.arcubit.particles.bits.Particle;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

/** Particle pool. */
public class BitPools 
{
	private static final ArrayMap<Class<?>, Pool<Particle>> pools;
	
	static {
		pools = new ArrayMap<Class<?>, Pool<Particle>>();
		Fragment.intsPool(pools);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized <T> T obtain(Class<T> type) {
		return (T)pools.get(type).obtain();
	}
	
	public static synchronized void free(Particle particle) {
		pools.get(particle.getClass()).free(particle);
	}
}
