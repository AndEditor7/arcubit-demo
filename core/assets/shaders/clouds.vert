attribute vec4 a_position;
attribute float a_light;

uniform mat4 u_projTrans;

varying float v_light;

void main() {
	v_light = a_light;
	gl_Position = u_projTrans * a_position;
}