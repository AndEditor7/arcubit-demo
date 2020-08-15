package com.andedit.arcubit.world.gen.structure;

public class StrutBuilder {
	
	private Structure strut;
	private boolean isBegin;
	
	public StrutBuilder() {
		
	}
	
	public void begin(int xSize, int ySize, int zSize) {
		isBegin = true;
		this.strut = new Structure(xSize, ySize, zSize);
	}
	
	public Structure end() {
		isBegin = false;
		return strut;
	}
	
	public void set(byte block, int x, int y, int z)
	{
		if (!isBegin) throw new IllegalStateException("StrutBuilder.begin must be called before building.");
		strut.data[x][y][z] = block;
	}
	
	public void fill(byte block, int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		if (!isBegin) throw new IllegalStateException("StrutBuilder.begin must be called before building.");
		++toX; ++toY; ++toZ;
		int data = 0;
		if (fromX > toX) {
			data = fromX;
			fromX = toX;
			toX = data;
			++toX;
			--fromX;			
		}
		if (fromY > toY) {
			data = fromY;
			fromY = toY;
			toY = data;
			++toY;
			--fromY;
		}
		if (fromZ > toZ) {
			data = fromZ;
			fromZ = toZ;
			toZ = data;
			++toZ;
			--fromZ;
		}
		for (int x = fromX; x < toX; ++x)
		{
			for (int y = fromY; y < toY; ++y)
			{
				for (int z = fromZ; z < toZ; ++z)
				{
					strut.data[x][y][z] = block;
				}
			}
		}
	}
}
