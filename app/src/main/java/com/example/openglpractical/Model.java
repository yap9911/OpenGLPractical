package com.example.openglpractical;

import android.opengl.GLES20;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.Map;

import glkit.BufferUtils;
import glkit.ShaderProgram;

public class Model {

    private static final int COORDS_PER_VERTEX = 3;
    private static final int COLORS_PER_VERTEX = 0;
    private static final int SIZE_OF_FLOAT = 4;
    private static final int SIZE_OF_SHORT = 2;

    private ShaderProgram shader;
    private String name;
    private float vertices[];
    private short indices[];

    Map<String, ObjLoader.Material> materials;
    private FloatBuffer vertexBuffer;
    private int vertexBufferId;
    private int vertexStride;

    private ShortBuffer indexBuffer;
    private int indexBufferId;

    protected Float3 position = new Float3(0f, 0f, 0f);   //translation
    protected float rotationX  = 0.0f;    //rotation
    protected float rotationY  = 0.0f;
    protected float rotationZ  = 0.0f;
    protected float scale      = 1.0f;    //scaling

    //viewing transformation
    protected Matrix4f camera = new Matrix4f();

    protected Matrix4f projection = new Matrix4f();



    //constructor
    //public Model(String name, ShaderProgram shader, float[] vertices, short[] indices, Map<String, ObjLoader.Material> materials) {
    public Model(String name, ShaderProgram shader, float[] vertices, short[] indices) {
            System.out.println("in Model Class creating model");
        //initialization
        this.name = name;
        this.shader = shader;
        this.vertices = Arrays.copyOfRange(vertices, 0, vertices.length);
        this.indices = Arrays.copyOfRange(indices, 0, indices.length);
        //this.materials = materials; // Add this line to store the materials map


        setupVertexBuffer();
        setupIndexBuffer();
    }


    public void setPosition(Float3 position) {
        this.position = position;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setProjection(Matrix4f mat) {
        projection.load(mat);
    }
    public void setCamera(Matrix4f mat) {
        camera.load(mat);  //to load martix (means multipying the new input to the camera)
        //like camera = mat
    }


    private void setupVertexBuffer() {
        vertexBuffer = BufferUtils.newFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        vertexBufferId = buffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * SIZE_OF_FLOAT, vertexBuffer,
                GLES20.GL_STATIC_DRAW);
        vertexStride = (COORDS_PER_VERTEX + COLORS_PER_VERTEX) * SIZE_OF_FLOAT; // 4 bytes per vertex
    }

    private void setupIndexBuffer() {
        // initialize index short buffer for index
        indexBuffer = BufferUtils.newShortBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);

        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        indexBufferId = buffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.length * SIZE_OF_SHORT,
                indexBuffer, GLES20.GL_STATIC_DRAW);
    }

    //sequence : translation -> rotaton -> scaling
    public Matrix4f modelMatrix() {
        Matrix4f mat = new Matrix4f(); // make a new identity 4x4 matrix
        mat.translate(position.x, position.y, position.z);
        mat.rotate(rotationX, 1.0f, 0.0f, 0.0f);
        mat.rotate(rotationY, 0.0f, 1.0f, 0.0f);
        mat.rotate(rotationZ, 0.0f, 0.0f, 1.0f);
        mat.scale(scale, scale, scale);
        return mat;
    }


    public void draw( long dt) {

        shader.begin();

        //have to multiply with the modelviewmatrix(the translation, scaling and rotation)
        //have to multiply the modelmatrix with the camera
        //viewing (camera) --> camera * modelMatrix() ==> camera matrix
        //modelMatrix() --> object/3D object (model-->world) ==> modelMatrix
        //both are 4*4 matrix, same format only can multiply
        camera.multiply(modelMatrix());
        shader.setUniformMatrix("u_ModelViewMatrix", camera);

        shader.setUniformMatrix("u_ProjectionMatrix", projection);

        // Apply material properties if available
//        if (materials != null && currentMaterial != null) {
//            shader.setUniformVector3("u_AmbientColor", currentMaterial.getAmbientColor());
//            shader.setUniformVector3("u_DiffuseColor", currentMaterial.getDiffuseColor());
//            shader.setUniformVector3("u_SpecularColor", currentMaterial.getSpecularColor());
//            // You may need to set additional material properties based on your shader program
//        }


        shader.enableVertexAttribute("a_Position");
        shader.setVertexAttribute("a_Position", COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,        // mode
                indices.length,             // count
                GLES20.GL_UNSIGNED_SHORT,   // type
                0);                         // offset

        shader.disableVertexAttribute("a_Position");

        shader.end();
    }

}
