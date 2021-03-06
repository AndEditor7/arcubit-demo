#version 100
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
	vec4 pixel = texture2D(u_texture, v_texCoords);
	if (pixel.a <= 0.0) discard;
	gl_FragColor = pixel;
}