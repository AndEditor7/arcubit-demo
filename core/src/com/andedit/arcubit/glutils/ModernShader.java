package com.andedit.arcubit.glutils;

import static com.badlogic.gdx.Gdx.gl30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ObjectIntMap;

public class ModernShader
{
	private static final IntBuffer intbuf = BufferUtils.newIntBuffer(1);
	
	/** Uniform lookup. */
	public final ObjectIntMap<String> uniforms = new ObjectIntMap<String>();

	/** Attribute lookup. */
	public final ObjectIntMap<String> attributes = new ObjectIntMap<String>();

	/** Program handle. **/
	public int program;

	/** Shader handles. **/
	public int vertexHandle, geometryHandle, fragmentHandle;

	public ModernShader (FileHandle vertexShader, FileHandle geometryShader, FileHandle fragmentShader) {
		compileShaders(vertexShader.readString(), geometryShader.readString(), fragmentShader.readString());
		fetchAttributes();
		fetchUniforms();
	}

	private void compileShaders (String vertexShader, String geometryShader, String fragmentShader) {
		vertexHandle = loadShader(GL30.GL_VERTEX_SHADER, vertexShader);
		geometryHandle = loadShader(0x8DD9, geometryShader); // GL_GEOMETRY_SHADER = 0x8DD9
		fragmentHandle = loadShader(GL30.GL_FRAGMENT_SHADER, fragmentShader);
		
		program = linkProgram(gl30.glCreateProgram());
	}

	private int loadShader (int type, String source) {
		final int shader = gl30.glCreateShader(type);
		
		gl30.glShaderSource(shader, source);
		gl30.glCompileShader(shader);
		gl30.glGetShaderiv(shader, GL30.GL_COMPILE_STATUS, intbuf);

		return shader;
	}

	private int linkProgram (int program) {
		gl30.glAttachShader(program, vertexHandle);
		gl30.glAttachShader(program, geometryHandle);
		gl30.glAttachShader(program, fragmentHandle);
		gl30.glLinkProgram(program);

		ByteBuffer tmp = ByteBuffer.allocateDirect(4);
		tmp.order(ByteOrder.nativeOrder());
		IntBuffer intbuf = tmp.asIntBuffer();

		gl30.glGetProgramiv(program, GL30.GL_LINK_STATUS, intbuf);

		return program;
	}
	
	public int fetchAttributeLocation (String name) {
		// -2 == not yet cached
		// -1 == cached but not found
		int location;
		if ((location = attributes.get(name, -2)) == -2) {
			location = gl30.glGetAttribLocation(program, name);
			attributes.put(name, location);
		}
		return location;
	}

	public int fetchUniformLocation (String name) {
		// -2 == not yet cached
		// -1 == cached but not found
		int location;
		if ((location = uniforms.get(name, -2)) == -2) {
			location = gl30.glGetUniformLocation(program, name);
			if (location == -1) {
				throw new IllegalStateException();
			}
			uniforms.put(name, location);
		}
		return location;
	}

	public void bind(){
		gl30.glUseProgram(program);
	}

	public void dispose () {
		gl30.glUseProgram(0);
		gl30.glDeleteShader(vertexHandle);
		gl30.glDeleteShader(geometryHandle);
		gl30.glDeleteShader(fragmentHandle);
		gl30.glDeleteProgram(program);
	}
	
	IntBuffer params = BufferUtils.newIntBuffer(1);
	IntBuffer type = BufferUtils.newIntBuffer(1);

	private void fetchUniforms () {
		params.clear();
		Gdx.gl20.glGetProgramiv(program, GL30.GL_ACTIVE_UNIFORMS, params);
		int numUniforms = params.get(0);

		for (int i = 0; i < numUniforms; i++) {
			params.clear();
			params.put(0, 1);
			type.clear();
			String name = Gdx.gl20.glGetActiveUniform(program, i, params, type);
			int location = Gdx.gl20.glGetUniformLocation(program, name);
			uniforms.put(name, location);
		}
	}

	private void fetchAttributes () {
		params.clear();
		Gdx.gl20.glGetProgramiv(program, GL30.GL_ACTIVE_ATTRIBUTES, params);
		int numAttributes = params.get(0);

		for (int i = 0; i < numAttributes; i++) {
			params.clear();
			params.put(0, 1);
			type.clear();
			String name = Gdx.gl20.glGetActiveAttrib(program, i, params, type);
			int location = Gdx.gl20.glGetAttribLocation(program, name);
			attributes.put(name, location);
		}
	}

	public void enableVertexAttribute(String alias) {
		gl30.glEnableVertexAttribArray(fetchAttributeLocation(alias));
	}
}
