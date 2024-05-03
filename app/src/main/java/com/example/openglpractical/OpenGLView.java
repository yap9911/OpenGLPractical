package com.example.openglpractical;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OpenGLView extends GLSurfaceView {


    private OpenGLRenderer renderer;


    // constructor with one parameter
    public OpenGLView(Context context) {
        super(context);
        init(context);
    }

    // constructor with two parameters
    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // to initialize the other features
    private void init(Context context) {
        // set OpenGL ES 2.0
        setEGLContextClientVersion(2);

        // store OpenGL context
        setPreserveEGLContextOnPause(true);

        renderer = new OpenGLRenderer(context); // Create your renderer

        // set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer); // Set the renderer for this view


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (renderer != null) {
            renderer.handleTouch(event);
        }
        return true;
    }
}
