attribute vec4 a_position;
attribute float a_light;
attribute vec2 a_texCoord;

uniform mat4 u_projTrans;
uniform float u_wave;

varying vec2 v_texCoords;

const float size = 1.4;

// a_power = 0.15
void main() {
	v_texCoords = a_texCoord;
	vec4 position = a_position;
	
	if (a_light > 0.01) {
		position.x += sin((a_position.x/size)+u_wave)*a_light;
	    position.z += cos((a_position.z/size)+u_wave)*a_light;
    }
	   
	gl_Position = u_projTrans * position;
}