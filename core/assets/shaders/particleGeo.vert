#version 310 es
#ifdef GL_ES
precision mediump float;
#endif

in vec4 a_position;
in vec4 a_texCoord;

out vec4 v_texCoords;

void main()
{
	v_texCoords = a_texCoord;
	gl_Position = a_position;
}
