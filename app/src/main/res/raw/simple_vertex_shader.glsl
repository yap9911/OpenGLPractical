uniform highp mat4 u_ProjectionMatrix;
uniform highp mat4 u_ModelViewMatrix;

attribute vec4 a_Position; // Per-vertex position information we will pass in. (x, y, z)
attribute vec2 a_TexCoord; // input for texture coordinate (u,v)
attribute vec3 a_Normal; // input for normal vector

varying vec4 v_Color; // This will be passed into the fragment shader.
varying vec2 frag_TexCoord;
varying lowp vec3 frag_Normal; // output to the fragment shader for normal vector
varying lowp vec3 frag_Position;

// The entry point for our vertex shader.
void main()
{

    frag_Normal = (u_ModelViewMatrix * vec4(a_Normal, 0.0)).xyz;
    frag_Position = (u_ModelViewMatrix * a_Position).xyz;

    // gl_Position is a special variable used to store the final position.
    // the final point in normalized screen coordinates.
    gl_Position = u_ProjectionMatrix * u_ModelViewMatrix * a_Position;

    frag_TexCoord = a_TexCoord; // set the output variable to be the input variable
}
