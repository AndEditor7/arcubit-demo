package com.andedit.arcubit.entity;

import com.andedit.arcubit.handle.Cam3D;
import com.andedit.arcubit.handle.GUI;
import com.andedit.arcubit.handle.Raycast;
import com.andedit.arcubit.handle.Raycast.RayInfo;
import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;

import static com.andedit.arcubit.block.Blocks.blocks;

import com.andedit.arcubit.block.Blocks;

public class Player
{
	public static Player player;
	
	public final World world;
	public final Cam3D cam;
	public boolean isFlying = false;
	
	public Player(Camera cam, World world) {
		this.cam = new Cam3D(cam);
		this.world = world;
		player = this;
	}
	
	public void update(RayInfo ray) {
		boolean breakB = false;
		boolean placeB = false;
		
		if (ray != null) {
			breakB = Gdx.input.isButtonJustPressed(Buttons.LEFT);
			placeB = Gdx.input.isButtonJustPressed(Buttons.RIGHT);
			
			if (breakB) {
				world.editable.breakBlock(ray.in,  blocks[Blocks.AIR]);
				Raycast.ray(player);
			} if (placeB) {
				world.editable.placeBlock(ray.out, blocks[GUI.blockPick]);
				Raycast.ray(player);
			}
		}
	}
}
