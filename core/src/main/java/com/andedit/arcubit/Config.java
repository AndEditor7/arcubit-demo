package com.andedit.arcubit;

import static com.badlogic.gdx.Gdx.*;

import com.andedit.arcubit.graphic.MeshVert;
import com.andedit.arcubit.graphic.QuadIndex;
import com.andedit.arcubit.graphic.vertex.VertBuf;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Collections;

/** Initializing/configuring stuffs.  */
class Config {
	static void init() {
		Collections.allocateIterators = true;
		ShaderProgram.pedantic = false;
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		gl.glCullFace(GL20.GL_BACK);
	}
}
