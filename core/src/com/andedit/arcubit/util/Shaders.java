package com.andedit.arcubit.util;

import java.util.Arrays;

import com.andedit.arcubit.mesh.verts.TerrainVert;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

@SuppressWarnings("unused")
public final class Shaders {
	public static final String projTran = "u_projTrans";
	
	public static ShaderProgram terrain;
	public static ShaderProgram plant;
	public static ShaderProgram water;
	public static int[] locations;
	
	public static void bindTerrain(Matrix4 combine) {
		terrain.bind();
		terrain.setUniformMatrix(projTran, combine);
	}
	
	private static float wavePlant;
	public static void bindPlant(Matrix4 combine) {
		wavePlant += 0.04f; // 0.06f
		if (wavePlant > MathUtils.PI2) {
			wavePlant -= MathUtils.PI2;
		}
		plant.bind();
		plant.setUniformMatrix(projTran, combine);
		plant.setUniformf("u_wave", wavePlant);
	}
	
	public static void bindWater(Matrix4 combine) {
		water.bind();
		water.setUniformMatrix(projTran, combine);
	}
	
	/** @return true if success. */
	public static boolean loadShaders() {
		try {
			terrain = new ShaderProgram(Util.getFile("shaders/terrain.vert"), Util.getFile("shaders/terrain.frag"));
			plant   = new ShaderProgram(Util.getFile("shaders/plant.vert"), Util.getFile("shaders/plant.frag"));
			water   = new ShaderProgram(Util.getFile("shaders/water.vert"), Util.getFile("shaders/water.frag"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		String log = "Shader log: \n";
		boolean error = false;
		if (!terrain.isCompiled()) {
			error = true;
			log += terrain.getLog() + "\n";
		}
		if (!plant.isCompiled()) {
			error = true;
			log += plant.getLog() + "\n";
		}
		if (!water.isCompiled()) {
			error = true;
			log += water.getLog();
		}
		if (error) {
			System.out.println(log);
		} else {
			locations = ShaderUtil.locateAttributes(terrain, TerrainVert.attributes);
		}
		return !error;
	}
}
