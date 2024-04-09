

//precision mediump float;
//void main() {
//    gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
//}

// Set the default precision to medium, don't need as high precision in the fragment shader.
precision mediump float;

// This is the color from the vertex shader interpolated across the triangle perfragment.
varying vec4 v_Color;

// The entry point for our fragment shader.
void main()
{
    // Pass through the color
    gl_FragColor = v_Color;
}
