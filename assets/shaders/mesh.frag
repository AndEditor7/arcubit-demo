#ifdef GL_ES
precision mediump float;
#endif

varying float v_light;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
	vec4 fragColor = texture2D(u_texture, v_texCoords);
	if (fragColor.a <= 0.0) discard;
	fragColor.rgb *= v_light;
	gl_FragColor = fragColor;
}