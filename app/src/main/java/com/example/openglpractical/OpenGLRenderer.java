package com.example.openglpractical;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.openglpractical.glkit.ShaderProgram;
import com.example.openglpractical.glkit.ShaderUtils;

public class OpenGLRenderer implements GLSurfaceView.Renderer {


    private ObjLoader objLoader;
    private float[] mvpMatrix = new float[16];

    private Context context;

    private ShaderProgram shader; // Declare as a class-level variable


    //animation parameters
    private static final float ONE_SEC = 1000.0f; // 1 second   //in miliseconds
    private long lastTimeMillis = 0L;

    private Model treeModel;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        System.out.println("on surface created");
        try {

            System.out.println("in openglrenderer obj loader");
            objLoader = new ObjLoader(context, "tree.obj", "tree.mtl");
            System.out.println("finish Objloader class");

            List<Float> verticesList = objLoader.getVertices();
            List<Integer> indicesList = objLoader.getIndices();
            System.out.println("finish Objloader.get vertices and indices class");

            // Convert lists to arrays
            float[] verticesArray = new float[verticesList.size()];
            for (int i = 0; i < verticesList.size(); i++) {

                verticesArray[i] = verticesList.get(i);
                //System.out.println(verticesArray[i]);

            }

            short[] indicesArray = new short[indicesList.size()];
            for (int i = 0; i < indicesList.size(); i++) {
                indicesArray[i] = indicesList.get(i).shortValue();
                //System.out.println(indicesArray[i]);
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
                //System.out.println(verticesArray[i]);

            }

            short[] indicesArray = new short[indicesList.size()];
            for (int i = 0; i < indicesList.size(); i++) {
                indicesArray[i] = indicesList.get(i).shortValue();
                //System.out.println(indicesArray[i]);

            }
//            System.out.println("the first vertices is");
//        System.out.println(verticesArray[0]);
//        System.out.println(verticesArray[verticesList.size()-1]);
//
//        System.out.println("the first indices is");
//        System.out.println(indicesArray[0]);
//        System.out.println(indicesArray[indicesList.size()-1]);


        shader = new ShaderProgram(
                    ShaderUtils.readShaderFileFromRawResource(context,
                            R.raw.simple_vertex_shader),
                    ShaderUtils.readShaderFileFromRawResource(context,
                            R.raw.simple_fragment_shader)
            );

        // Create and configure the model object
        //load materialsmap
        //the model colorspervertices got problem
        //treeModel = new Model("tree", shader, verticesArray, indicesArray, materialsMap);
        treeModel = new Model("tree", shader, verticesArray, indicesArray);

        // Set the position, rotation, and scale of the model if needed
            treeModel.setPosition(new Float3(0.0f, 0.0f, 0.0f));
            treeModel.setRotationX(0.0f);
            treeModel.setRotationY(0.0f);
            treeModel.setRotationZ(0.0f);
            treeModel.setScale(1.0f);

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

        if(treeModel != null) {
            Matrix4f perspective = new Matrix4f();

            //perspective setting
            perspective.loadPerspective(85.0f, aspectRatio, 1.0f, 150.0f); //(field of view, aspect ratio, near z, far z)

            //then uodate to your square
            treeModel.setProjection(perspective);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //System.out.println("on draw frame");

        GLES20.glClearColor(0.2f, 0.2f, 0.5f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //obtain the latest time of the system
        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis; //update last time with current time

        //System.out.println("finish draw frame");

    }

    public void updateWithDelta(long dt) {

        final float secsPerMove = 2.0f * ONE_SEC;

        float movement = (float)(Math.sin(System.currentTimeMillis() * 2 * Math.PI /
                secsPerMove));

        Matrix4f camera = new Matrix4f();
        //adjustment to camera //since its 4*4, they offer the translation, rotate...methods
        //camera.translate(0.0f, (float)(-1.0f * movement), 0.0f);
        //if the z value is 0.0, its too front, so we move further away to -5.0 to be able to see the square
        camera.translate(0.0f, (float)(-1.0f * movement), -5.0f);
        camera.rotate(360.0f * movement, 0.0f, 0.0f, 1.0f);
        camera.scale(movement, movement, movement);
        //square.setCamera(camera);
        //square.draw(dt);

        //this camera2 will make the cube rotate in position
        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        treeModel.setCamera(camera2);
        treeModel.setRotationY((float)( treeModel.rotationY + Math.PI * dt / (ONE_SEC * 0.1f) ));
        treeModel.setRotationZ((float)( treeModel.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f) ));
        treeModel.draw(dt);
    }

    public OpenGLRenderer(Context context) {
        this.context = context;
    }
}

