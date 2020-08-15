#version 310 es
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

in vec2 v_texCoordFrag;

out vec4 fragmentColor;

void main()
{
	fragmentColor = texture(u_texture, v_texCoordFrag);
}
