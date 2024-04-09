

//attribute vec4 a_Position;
//void main() {
//    gl_Position = a_Position;
//}

uniform highp mat4 u_ProjectionMatrix;

uniform highp mat4 u_ModelViewMatrix;


attribute vec4 a_Position;    // Per-vertex position information we will pass in.
attribute vec4 a_Color;       // Per-vertex color information we will pass in.

varying vec4 v_Color; // This will be passed into the fragment shader.

// The entry point for our vertex shader.
void main()
{
    // Pass through the color.
    v_Color = a_Color;

    // gl_Position is a special variable used to store the final position.
    // the final point in normalized screen coordinates.
    //gl_Position = a_Position;

    //p8
    //gl_Position = u_ModelViewMatrix * a_Position;
    gl_Position = u_ProjectionMatrix * u_ModelViewMatrix * a_Position;

}