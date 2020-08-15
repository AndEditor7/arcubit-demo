package com.andedit.arcubit.particles.bits;

import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

/** Test particle. */
public class Fragment extends Particle 
{
	public static void intsPool(ArrayMap<Class<?>, Pool<Particle>> pools) {
		pools.put(Fragment.class, new Pool<Particle>() { @Override
			protected Particle newObject() {
				return new Fragment();
			}
		});
	}
	
	private static final Vector2 tmp2 = new Vector2();
	
	private final Vector3 vel = new Vector3();
	private float deathY;
	
	public Fragment ints(Vector3 pos, TextureRegion side, float power) {
		this.pos.set(pos);
		deathY = pos.y-1f;
		tmp2.setToRandomDirection().scl(MathUtils.random(0.25f*power));
		vel.set(tmp2.x, (0.5f+MathUtils.random(0.4f))*power, tmp2.y);
		region.setRegion(side);
		isDead = false;
		return this;
	}

	@Override
	public void update(World world) {
		if (deathY > pos.y) {
			isDead = true;
			return;
		}
		vel.y -= 0.01f;
		pos.add(vel);
	}
	
	public static class FragmentInfo implements PartInfo<Fragment> 
	{
		private final Vector3 pos = new Vector3();
		
		@Override
		public Fragment ints(Fragment part) {
			
			return part;			
		}
	}
}
