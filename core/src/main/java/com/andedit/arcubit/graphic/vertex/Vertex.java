package com.andedit.arcubit.graphic.vertex;

import static com.andedit.arcubit.graphic.vertex.VertBuf.buffer;

import java.nio.ByteBuffer;

import com.andedit.arcubit.util.Util;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;

/** Vertex Data */
public interface Vertex extends Disposable {
	void setVertices(float[] array, int size, int offset);
	void bind();
	void unbind();
	void setDraw(int glDraw);
	int getDraw();
	int size();

	static Vertex newVbo(VertContext context, int draw) {
		return Util.isGL30() ? new VAO(context, draw) : new VBO(context, draw);
	}
	
	static Vertex newVa(VertContext context) {
		return newVa(context, buffer);
	}
	
	static Vertex newVa(VertContext context, ByteBuffer buffer) {
		return Util.isGL30() ? new VAO(context, GL20.GL_DYNAMIC_DRAW) : new VA(context, buffer);
	}
}
