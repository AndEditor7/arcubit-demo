attribute vec4 a_position;
attribute float a_light;
attribute vec2 a_texCoord;

uniform mat4 u_projTrans;

varying float v_light;
varying vec2 v_texCoords;

void main() {
	v_light = a_light;
	v_texCoords = a_texCoord;
	gl_Position = u_projTrans * a_position;
}