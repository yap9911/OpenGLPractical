package com.example.openglpractical;

import static com.example.openglpractical.glkit.ShaderUtils.readShaderFileFromRawResource;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.example.openglpractical.glkit.BufferUtils;
import com.example.openglpractical.glkit.ShaderProgram;

public class Triangle {

    private FloatBuffer vertexBuffer;
    private ShaderProgram shader;
    private int vertexBufferId;
    private int vertexCount;
    private int vertexStride;

    // number of coordinates per vertex in this array
    // 3 means xyz
    static final int COORDS_PER_VERTEX = 3;

    //when u define the vertices, they are all located in cpu //between -1 to 1 (normalized range)
    static final float[] triangleCoords = {
            0.0f,  0.25f, 0.0f,    // TOP
            -0.5f, -0.25f, 0.0f,    // LEFT
            0.5f, -0.25f, 0.0f,    // RIGHT
    };

    static final float [] vertices = {
            0.37f, -0.12f, 0.0f, // A
            0.95f,  0.30f, 0.0f, // B
            0.23f,  0.30f, 0.0f, // C

            0.23f,  0.30f, 0.0f, // C
            0.00f,  0.90f, 0.0f, // D
            -0.23f,  0.30f, 0.0f, // E

            -0.23f,  0.30f, 0.0f, // E
            -0.95f,  0.30f, 0.0f, // F
            -0.37f, -0.12f, 0.0f, // G

            -0.37f, -0.12f, 0.0f, // G
            -0.57f, -0.81f, 0.0f, // H
            0.00f, -0.40f, 0.0f, // I

            0.00f, -0.40f, 0.0f, // I
            0.57f, -0.81f, 0.0f, // J
            0.37f, -0.12f, 0.0f, // A
    };

    public Triangle(Context context) {
        setupShader(context);
        setupVertexBuffer();
    }

    private void setupShader(Context context) {
        // compile & link shader
        shader = new ShaderProgram(
                readShaderFileFromRawResource(context,
                        R.raw.simple_vertex_shader),
                readShaderFileFromRawResource(context,
                        R.raw.simple_fragment_shader)
        );
    }

    private void setupVertexBuffer() {
        // initialize vertex float buffer for shape coordinates
        // conversion
        //vertexBuffer = BufferUtils.newFloatBuffer(triangleCoords.length);

        vertexBuffer = BufferUtils.newFloatBuffer(vertices.length);

        // add the coordinates to the FloatBuffer
        //((FloatBuffer) vertexBuffer).put(triangleCoords);
        ((FloatBuffer) vertexBuffer).put(vertices);


        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        //copy vertices from cpu to the gpu
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        vertexBufferId = buffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, triangleCoords.length * 4, vertexBuffer,
//                GLES20.GL_STATIC_DRAW);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer,
                GLES20.GL_STATIC_DRAW);

        //to produce how many vertices they have to draw
        //vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
        vertexCount = vertices.length / COORDS_PER_VERTEX;

        // 4 bytes per vertex so need *4
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    }

    public void draw() {

        shader.begin();

        // to activate vertex shader //a_position come from vertex shader
        shader.enableVertexAttribute("a_Position");
        shader.setVertexAttribute("a_Position", COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        shader.disableVertexAttribute("a_Position");

        shader.end();
    }

}
