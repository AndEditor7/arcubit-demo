package com.andedit.arcubit.entity;

import com.andedit.arcubit.graphic.Camera;
import com.andedit.arcubit.input.contoller.GameController;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class Player {
	
	private final Camera camera;
	private final GameController controller;
	
	public Player(Camera camera, GameController controller) {
		this.camera = camera;
		this.controller = controller;
		
		camera.position.set(World.SIZE/2, World.HEIGHT/2, World.SIZE/2);
	}
	
	public void update() {
		camera.fieldOfView = 60;
		Vector2 look = controller.getLook();
		camera.yaw += look.x;
		camera.pitch += look.y;
		float scl = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) ? 2 : 1;
		scl *= 0.4f;
		Vector2 move = controller.getMove().rotateDeg(-camera.yaw).scl(2f);
		camera.translate(move.x * scl, controller.getMoveY()  * scl, move.y * scl);
		camera.updateRotation();
	}
}
