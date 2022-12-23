#ifdef GL_ES
precision highp float;
#endif

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_data;

uniform mat4 u_projTrans;

varying float v_light;
varying vec2 v_texCoords;

void main() {
	v_light = a_data.r;
	v_texCoords = a_texCoord0;
	gl_Position = u_projTrans * a_position;
}