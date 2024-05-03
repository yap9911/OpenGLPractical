package com.example.openglpractical;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.openglpractical.glkit.ShaderProgram;
import com.example.openglpractical.glkit.ShaderUtils;
import com.example.openglpractical.glkit.TextureUtils;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private ObjLoader objLoader;
    private float[] mvpMatrix = new float[16];

    private Context context;

    private ShaderProgram shader; // Declare as a class-level variable
    private long lastTimeMillis = 0L;

    private Model ObjModel;

    private float previousX, previousY;
    private float rotationAngleX;
    private float rotationAngleY;
    private float scaleFactor = 1.0f;
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId1 = INVALID_POINTER_ID;
    private float initialDistance = 0.0f;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        System.out.println("on surface created");
        try {

            System.out.println("in openglrenderer obj loader");
            objLoader = new ObjLoader(context, "mushroom.obj", "mushroom.mtl");
            System.out.println("finish Objloader class");

            List<Float> verticesList = objLoader.getVertices();
            List<Integer> indicesList = objLoader.getIndices();
            System.out.println("finish Objloader.get vertices and indices class");

            // Convert lists to arrays
            float[] verticesArray = new float[verticesList.size()];
            for (int i = 0; i < verticesList.size(); i++) {
                verticesArray[i] = verticesList.get(i);
            }

            short[] indicesArray = new short[indicesList.size()];
            for (int i = 0; i < indicesList.size(); i++) {
                indicesArray[i] = indicesList.get(i).shortValue();
            }

            System.out.println("finish OpenglRenderer");


        } catch (IOException e) {
            e.printStackTrace();
        }

            List<Float> verticesList = objLoader.getVertices();
            List<Integer> indicesList = objLoader.getIndices();

            // Convert lists to arrays
            float[] verticesArray = new float[verticesList.size()];
            for (int i = 0; i < verticesList.size(); i++) {
                verticesArray[i] = verticesList.get(i);

            }

            short[] indicesArray = new short[indicesList.size()];
            for (int i = 0; i < indicesList.size(); i++) {
                indicesArray[i] = indicesList.get(i).shortValue();

            }

        Map<String, ObjLoader.Material> materialsMap = objLoader.getMaterials();

        shader = new ShaderProgram(
            ShaderUtils.readShaderFileFromRawResource(context,
                    R.raw.simple_vertex_shader),
            ShaderUtils.readShaderFileFromRawResource(context,
                    R.raw.simple_fragment_shader)
        );

        int Texture = TextureUtils.loadTexture(context, R.drawable.mushroom);

        // Create and configure the model object
        // load materialsmap

        ObjModel = new Model("mushroom", shader, verticesArray, indicesArray, materialsMap);

        // Set the position, rotation, and scale of the model if needed
        ObjModel.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        ObjModel.setRotationX(102.0f);
        ObjModel.setRotationY(180.0f);
        ObjModel.setRotationZ(255.0f);
        ObjModel.setScale(scaleFactor);
        ObjModel.setTexture(Texture);

        //extract the current time of the system in miliseconds
        lastTimeMillis = System.currentTimeMillis();

        System.out.println("finish onsurfacecreate");

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


        GLES20.glViewport(0, 0, width, height);
        float aspectRatio = (float)width / (float)height;
        GLES20.glViewport(0, 0, width, height);
        System.out.println("on surface changed");

        if(ObjModel != null) {
            Matrix4f perspective = new Matrix4f();

            //perspective setting
            perspective.loadPerspective(85.0f, aspectRatio, 1.0f, 150.0f); //(field of view, aspect ratio, near z, far z)

            ObjModel.setProjection(perspective);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //System.out.println("on draw frame");

        GLES20.glClearColor(0.2f, 0.2f, 0.5f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -6.0f);
        ObjModel.setCamera(camera2);

        ObjModel.draw();

    }

    public void handleTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //when one finger touch screen
                activePointerId1 = event.getPointerId(0);
                //save the current model x and y coordinates before transformtion
                previousX = x;
                previousY = y;
                System.out.println(event.getPointerCount());
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //when two fingers touch screen
                previousX = x;
                previousY = y;
                if (event.getPointerCount() == 2) {
                    float dx = event.getX(0) - event.getX(1);
                    float dy = event.getY(0) - event.getY(1);
                    initialDistance = (float) Math.sqrt(dx * dx + dy * dy);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {

                    //translation
                    float dx = x - previousX;
                    float dy = y - previousY;

                    float sensitivity = 0.05f;

                    // Translate the model along X and Y axes based on finger movement
                    Float3 newPosition = new Float3(
                            ObjModel.position.x + dx * sensitivity,
                            ObjModel.position.y - dy * sensitivity,
                            ObjModel.position.z
                    );
                    ObjModel.setPosition(newPosition);
                    // Update previous touch coordinates for translation
                    previousX = x;
                    previousY = y;


                } else if (event.getPointerCount() == 2) {

                    //scaling
                    float dx1 = event.getX(0) - event.getX(1);
                    float dy1 = event.getY(0) - event.getY(1);
                    float currentDistance = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);

                    float scaleChange = currentDistance - initialDistance;

                    float sensitivity = 0.005f;
                    float scaleFactor = 1 + scaleChange * sensitivity;

                    float minScaleFactor = 0.3f;
                    float maxScaleFactor = 3.0f;

                    //min and max scaling factor
                    if (scaleFactor < minScaleFactor) {
                        scaleFactor = minScaleFactor;
                    }
                    if (scaleFactor > maxScaleFactor) {
                        scaleFactor = maxScaleFactor;
                    }

                    ObjModel.setScale(scaleFactor);

                    //rotation
                    float dx = x - previousX;
                    float dy = y - previousY;

                    float sensitivity2 = 0.02f;

                    float angleDegreesX = dx * sensitivity2; // Rotate around Y-axis based on horizontal movement
                    float angleDegreesY = dy * sensitivity2; // Rotate around X-axis based on vertical movement
                    rotationAngleX = (rotationAngleX + angleDegreesY) % 360; // Rotate around X-axis (up/down)
                    rotationAngleY = (rotationAngleY + angleDegreesX) % 360; // Rotate around Y-axis (left/right)

                    ObjModel.setRotationX(rotationAngleX);
                    ObjModel.setRotationY(rotationAngleY);
                }
                break;
        }
    }


    public OpenGLRenderer(Context context) {
        this.context = context;
    }
}

