package com.andedit.arcubit.block;

public class AirBlock extends Block {
	
	public AirBlock() {
		super(new Settings().noCollision());
	}
	
	@Override
	public boolean isAir() {
		return true;
	}
}
