package com.andedit.arcubit.handle;

import static com.andedit.arcubit.world.World.defaultSize;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.andedit.arcubit.chunk.Chunk;
import com.andedit.arcubit.chunk.ChunkRegion;
import com.andedit.arcubit.util.FileUtil;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;;

//TODO: Rework this world save system.
public class Saver 
{
	public static void save(World world)
	{
		DisplayMode currentMode = Gdx.graphics.getDisplayMode();
		boolean call = false;
		if (Gdx.graphics.isFullscreen())
		{
			currentMode = Gdx.graphics.getDisplayMode();
			Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
			call = true;
		}
		
		File selectedFile = null;
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new WorldFilter();
		
		chooser.setFileFilter(filter);
		if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		    selectedFile = chooser.getSelectedFile();
		}
		
		if (selectedFile != null)
		{
			int xSize = defaultSize*Chunk.SIZE;
			int ySize = ChunkRegion.LENGTH*Chunk.SIZE;
			int zSize = defaultSize*Chunk.SIZE;
			
			//int xFix = xSize/2;
			//int zFix = zSize/2;
			
			byte[] blocks = new byte[xSize*ySize*zSize];
			int i = 0;
			for (int x = 0; x < xSize; x++)
			{
				for (int y = 0; y < ySize; y++)
				{
					for (int z = 0; z < zSize; z++)
					{
						blocks[i] = world.getBlock(x, y, z);
						i++;
					}
				}
			}
			
			String path = selectedFile.getAbsolutePath();
			if (FileUtil.extensionOf("mclone", path)) {
				Gdx.files.absolute(path).writeBytes(blocks, false);
			} else {
				Gdx.files.absolute(FileUtil.setExtension("mclone", path)).writeBytes(blocks, false);
			}
			if (call) Gdx.graphics.setFullscreenMode(currentMode);
			return;
		}
		
		System.out.println("Could not save.");
		
		System.gc();
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (call) Gdx.graphics.setFullscreenMode(currentMode);
	}
	
	public static World load()
	{		
		File selectedFile = null;
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new WorldFilter();
		
		chooser.setFileFilter(filter);
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    selectedFile = chooser.getSelectedFile();
		}
		
		byte[] blocks = null;
		if (selectedFile != null) {
			try {
				blocks = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
			} catch (Exception e) {
				e.printStackTrace();
				Gdx.app.exit();
			}
			// blocks = Gdx.files.absolute(selectedFile.getAbsolutePath()).readBytes();
		} else {
			return new World(true);
		}
		
		
		World world = new World(false);
		int xSize = defaultSize*Chunk.SIZE;
		int ySize = ChunkRegion.HEIGHT;
		int zSize = defaultSize*Chunk.SIZE;
		
		int i = 0;
		for (int x = 0; x < xSize; x++)
		{
			for (int y = 0; y < ySize; y++)
			{
				for (int z = 0; z < zSize; z++)
				{
					//world.setBlock(x-xFix, y, z-zFix, blocks[i++]);
					world.setBlock(x, y, z, blocks[i++]);
				}
			}
		}
		
		/* TODO: lighting disabled.
		for (int x = 0; x < defaultSize; x++)
		{
			for (int z = 0; z < defaultSize; z++)
			{
				world.regions[x][z].reLighting();;
			}
		} */
		
		return world;
	}
}
