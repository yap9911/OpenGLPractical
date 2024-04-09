package com.example.openglpractical;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {

    // constructor with one parameter
    public OpenGLView(Context context) {
        super(context);
        init();
    }

    // constructor with two parameters
    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // to initialize the other features
    private void init() {
        // set OpenGL ES 2.0
        setEGLContextClientVersion(2);

        // store OpenGL context
        setPreserveEGLContextOnPause(true);

        // set the Renderer for drawing on the GLSurfaceView
        setRenderer(new OpenGLRenderer(getContext()));


    }
}
