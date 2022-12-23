package com.andedit.arcubit.world.gen;

import com.andedit.arcubit.world.World;

/** World generator */
@FunctionalInterface
public interface Generator {
	void gen(World world);
}
