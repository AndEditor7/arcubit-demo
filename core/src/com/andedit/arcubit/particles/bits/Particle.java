package com.andedit.arcubit.particles.bits;

import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/** Low level particle. */
public abstract class Particle 
{
	public final Vector3 pos = new Vector3();
	public final TextureRegion region = new TextureRegion();
	public boolean isDead = false;
	
	public abstract void update(World world);
	
	public static interface PartInfo<Part extends Particle> {
		public Part ints(Part part);
	}
}
