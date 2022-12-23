package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.graphic.Camera;
import com.andedit.arcubit.input.contoller.DesktopController;
import com.andedit.arcubit.input.contoller.GameController;
import com.andedit.arcubit.util.Util;
import com.andedit.arcubit.world.Renderer;
import com.andedit.arcubit.world.World;
import com.andedit.arcubit.world.gen.DefaultGen;
import com.andedit.arcubit.world.gen.SimpleGen;
import com.badlogic.gdx.ScreenAdapter;

/** The main game instance */
public class TheGame extends ScreenAdapter {
	
	private final Camera camera;
	private final Player player;
	private final GameController controller;
	private final World world;
	private final Renderer render;
	
	public TheGame() {
		world = new World();
		new DefaultGen().gen(world);
		render = new Renderer();
		render.setWorld(world);
		camera = new Camera();
		camera.setView(Util.getW(), Util.getH());
		camera.far = 500;
		controller = new DesktopController();
		player = new Player(camera, controller);
	}
	
	@Override
	public void show() {
		main.setCatched(true);
		main.inputs.addProcessor(controller.getInput());
	}
	
	@Override
	public void render(float delta) {
		Util.glClear();
		
		player.update();
		camera.update();
		render.render(camera);
		
		controller.clear();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setView(width, height);
	}
	
	@Override
	public void dispose() {
		render.dispose();
	}
}
