package com.andedit.arcubit.particles.batchs;

import com.andedit.arcubit.particles.bits.Particle;
import com.badlogic.gdx.utils.Disposable;

/** Basic particle renderer. */ 
public interface IParticleRenderer extends Disposable 
{
	/** Start the particle renderer. */
	public void begin();
	/** Flush/render the particles. */
	public void flush();
	/** End the particle renderer and render it. */
	public void end();
	/** Draw the particle. */
	public void draw(Particle particle);
}
