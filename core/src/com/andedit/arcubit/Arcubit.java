package com.andedit.arcubit;

import static com.andedit.arcubit.block.Blocks.blocks;
import static com.andedit.arcubit.util.Util.screen;

import com.andedit.arcubit.block.Blocks;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.handle.GUI;
import com.andedit.arcubit.handle.InputHolder;
import com.andedit.arcubit.handle.Inputs;
import com.andedit.arcubit.handle.Raycast;
import com.andedit.arcubit.handle.Raycast.RayInfo;
import com.andedit.arcubit.handle.Saver;
import com.andedit.arcubit.renderer.BoxRenderer;
import com.andedit.arcubit.renderer.TexLib;
import com.andedit.arcubit.util.Camera;
import com.andedit.arcubit.util.GdxUtil;
import com.andedit.arcubit.util.Shaders;
import com.andedit.arcubit.util.Util;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Arcubit extends ApplicationAdapter 
{
	private Camera cam;
	private Inputs input;
	private BitmapFont font;
	
	public World world;
	public Player player;
	public ScreenViewport view = new ScreenViewport();
	
	private Matrix4 combined;
	private SpriteBatch batch;
	
	public Arcubit(World world2) 
	{
		world = world2;
	}

	@Override
	public void create () 
	{
		screen.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam = new Camera(70, screen.w, screen.h);
        cam.lookAt(500,30,500);
        cam.near = 0.2f;
        cam.far = 600f; // 300f
        cam.update();
		normalSartup();
		
		boolean fix = true;
		createWorld(fix);
		final int center = World.defaultSize*8;
		for (int y = ChunkRegion.HEIGHT-1; y > -1; y--) {
			if (blocks[world.getBlock(center, y, center)].collision) {
				cam.position.set(center, y-6, center);
				break;
			}
		}
		batch = new SpriteBatch(100);
		combined = view.getCamera().combined;
		
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(new InputMultiplexer(new InputHolder(), input = new Inputs()));
		Inputs.resetMouse();
		GUI.ints();
		
		box = new BoxRenderer(cam);
		
		player = new Player(cam, world);
	}
	
	BoxRenderer box;

	@Override
	public void render () 
	{
		try {
			update();
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
			return;
		}
	}
	
	GLProfiler per;
	private void update()
	{
		GdxUtil.closeOnEsc();
		if (Inputs.isKeyJustPressed(Keys.F4)) {
			Saver.save(world);
		}
		
		Gdx.gl.glUseProgram(0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		if (Inputs.isKeyJustPressed(Keys.P)) {
			PartiBench.exec(cam);
		}
		
		RayInfo ray = null;
		if (!PartiBench.isBench) {
			player.cam.move();
			ray = Raycast.ray(player);
			player.update(ray);
		} else {
			PartiBench.run(cam);
		}
		cam.update();
		
		world.render(cam);
		box.render(ray);
		
		batch.setProjectionMatrix(combined);
		batch.begin();
		GUI.render(batch);
		font.draw(batch, Integer.toString(world.parts.getSize()), 10, 10);
		batch.end();
		
		input.clearJustPressed();
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height, true);
		Util.screen.set(width, height);
		Util.world.set(MathUtils.roundPositive(view.getWorldWidth()), MathUtils.roundPositive(view.getWorldHeight()));
		input.clear();
		cam.viewportWidth = width;
		cam.viewportHeight = height;
	}
	
	@Override
	public void dispose () {
		GdxUtil.disposes(batch, font, box); 
		if (TexLib.atlas != null) TexLib.atlas.dispose();
		world.dispose();
	}
	
	
	// ######### StartUP ############
	
	private void createWorld(boolean fix) {
		long a = System.currentTimeMillis();
		if (world != null) return;
		System.out.println(System.currentTimeMillis()-a);
		System.gc();
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void normalSartup()
	{
		font = new BitmapFont(Util.getFile("font/Mozart.fnt"), Util.getFile("font/Mozart.png"), false, true);
		TexLib.loadTexture();
		Blocks.loadTextures();
		Shaders.loadShaders();
		world.intsRender(cam, 24); // 10
	    OpenGL();
	}
	
	private void OpenGL() {
		final GL20 gl = Gdx.gl;
		
        gl.glClearColor(0.45f, 0.60f, 0.94f, 1);
		
		gl.glLineWidth(2);
		
		gl.glCullFace(GL20.GL_BACK);
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glDepthFunc(GL20.GL_LEQUAL);
	}
}
