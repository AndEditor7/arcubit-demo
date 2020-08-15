#version 310 es
#extension GL_EXT_geometry_shader : enable

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

// [x,y,z,w] are [u,v,u2,v2]
in vec4 v_texCoords[];

out vec2 v_texCoordFrag;

uniform mat4 u_projTrans;
uniform vec3 u_camPos;
uniform vec3 up;

const float size = 0.2;

void main()
{	
	vec3 pos = gl_in[0].gl_Position.xyz;
	vec3 right = cross(normalize(u_camPos - pos), up);
	
	vec3 va = pos - (right + up) * size; // v1
	gl_Position = u_projTrans * vec4(va, 1.0);
  	v_texCoordFrag = vec2(v_texCoords[0].z, v_texCoords[0].w);
	EmitVertex();
	
	vec3 vb = pos - (right - up) * size; // v2
	gl_Position = u_projTrans * vec4(vb, 1.0);
	v_texCoordFrag = vec2(v_texCoords[0].z, v_texCoords[0].y);
	EmitVertex();
	
	vec3 vd = pos + (right - up) * size; // v3
	gl_Position = u_projTrans * vec4(vd, 1.0);
	v_texCoordFrag = vec2(v_texCoords[0].x, v_texCoords[0].w);
	EmitVertex();
	
	vec3 vc = pos + (right + up) * size; // v4
	gl_Position = u_projTrans * vec4(vc, 1.0);
	v_texCoordFrag = vec2(v_texCoords[0].x, v_texCoords[0].y);
	EmitVertex();
	
	EndPrimitive();
}