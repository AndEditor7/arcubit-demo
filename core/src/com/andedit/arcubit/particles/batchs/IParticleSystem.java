package com.andedit.arcubit.particles.batchs;

import com.andedit.arcubit.particles.bits.Particle;
import com.badlogic.gdx.utils.Disposable;

/** The particle system interface. */
public interface IParticleSystem extends Disposable 
{
	/** Update and render the particles. */
	public void render();
	/** Add new particle to this system. */
	public void add(Particle part);
	/** Get the total particles. */
	public int getSize();
}
