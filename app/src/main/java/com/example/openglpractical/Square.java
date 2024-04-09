package com.example.openglpractical;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import glkit.BufferUtils;
import glkit.ShaderProgram;
import glkit.ShaderUtils;

//public class Square {
//
//    private FloatBuffer vertexBuffer;
//    private ShaderProgram shader;
//    private int vertexBufferId;
//    private int vertexCount;
//    private int vertexStride;
//
//    private ShortBuffer indexBuffer;
//    private int indexBufferId;
//
//    // number of coordinates per vertex in this array
//    static final int COORDS_PER_VERTEX = 3;
//    static final int COLORS_PER_VERTEX = 4;
//    static final int SIZE_OF_FLOAT = 4;
//
//    static final int SIZE_OF_SHORT = 2;
////    static final float[] squareCoords = {
////            -0.5f,  0.5f, 0.0f,   // top left
////            -0.5f, -0.5f, 0.0f,   // bottom left
////            0.5f, -0.5f, 0.0f,   // bottom right
////
////            -0.5f,  0.5f, 0.0f,   // top left
////            0.5f, -0.5f, 0.0f,   // bottom right
////            0.5f,  0.5f, 0.0f    // top right
////    };
//
////    static final float[] squareCoords = {
////            -0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,// top left
////            -0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,// bottom left
////            0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f,// bottom right
////
////            -0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,// top left
////            0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f,// bottom right
////            0.5f,  0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,// top right
////    };
//
//    //this data is stored in CPU
//    static final float[] squareCoords = {
//            -0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,// top left
//            -0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,// bottom left
//            0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f,// bottom right
//            0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,// top right
//    };
//
//    //this data is stored in CPU
//    // thats why later will need to copy back to the GPU
//    static final short[] indices = {
//            0, 1, 2,
//            0, 2, 3,
//    };
//
//    public Square(Context context) {
//        setupShader(context);
//        setupVertexBuffer();
//        setupIndexBuffer();
//    }
//
//    private void setupShader(Context context) {
//        // compile & link shader
//        shader = new ShaderProgram(
//                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_vertex_shader),
//                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_fragment_shader)
//        );
//    }
//
//    private void setupVertexBuffer() {
//        // initialize vertex float buffer for shape coordinates
//        vertexBuffer = BufferUtils.newFloatBuffer(squareCoords.length);
//
//        // add the coordinates to the FloatBuffer
//        vertexBuffer.put(squareCoords);
//
//        // set the buffer to read the first coordinate
//        vertexBuffer.position(0);
//
//        //copy vertices from cpu to the gpu
//        IntBuffer buffer = IntBuffer.allocate(1);
//        GLES20.glGenBuffers(1, buffer);
//        vertexBufferId = buffer.get(0);
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, squareCoords.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);
//
//        //vertexCount = squareCoords.length / COORDS_PER_VERTEX;
//        //vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
//
//        vertexCount = squareCoords.length / (COORDS_PER_VERTEX + COLORS_PER_VERTEX);
//        vertexStride = (COORDS_PER_VERTEX + COLORS_PER_VERTEX) * 4; // 4 bytes per vertex
//    }
//
//    private void setupIndexBuffer() {
//        // initialize index short buffer for index
//        // conversion from the android data type --> opengl es data type
//        // short[] indices --> shortbuffer
//        indexBuffer = BufferUtils.newShortBuffer(indices.length);
//        //copy the content in indices
//        indexBuffer.put(indices);
//        // rewind/ reposition to the beginning
//        indexBuffer.position(0);
//
//        // allocate a memory for the indices in GPU
//        IntBuffer buffer = IntBuffer.allocate(1);
//
//        // generate 1 memory slot: buffer object in GPU
//        GLES20.glGenBuffers(1, buffer);
//        indexBufferId = buffer.get(0);
//        //bind the buffer object with a target: GL_ELEMENT_ARRAY_BUFFER --> BUFFER OBJECT WILL BECOME EBO (element buffer object)
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
//        // begin to copy the indices in CPU to GPU
//        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.length * SIZE_OF_SHORT,
//                indexBuffer, GLES20.GL_STATIC_DRAW);
//
//        // EBO (GPU) // very tight, no waste space
//        // |0|1|2|0|2|3|
//    }
//
//    public void draw() {
//
//        shader.begin();
//
//        shader.enableVertexAttribute("a_Position");
//        shader.setVertexAttribute("a_Position", COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, 0);
//
//        shader.enableVertexAttribute("a_Color");
//
//        shader.setVertexAttribute("a_Color", COLORS_PER_VERTEX, GLES20.GL_FLOAT, false,
//                vertexStride, COORDS_PER_VERTEX * SIZE_OF_FLOAT);
//
//
//
//        // ordered drawing --> primitive asssembly
//        // target: GL_ARRAY_BUFFER
//        //GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
//        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
//
//        // indexed drawing --> primitive assembly stage
//        // updated VBO with colour attributes
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
//        GLES20.glDrawElements(
//                GLES20.GL_TRIANGLES,// mode
//                indices.length,// count
//                GLES20.GL_UNSIGNED_SHORT, //type
//                0); //offset
//
//        //once finished drawing, have to disable the vertex attribute
//        shader.disableVertexAttribute("a_Position");
//        shader.disableVertexAttribute("a_Color");
//
//        shader.end();
//    }
//}

public class Square extends Model{

//    static final float[] squareCoords = {
//            -0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,// top left
//            -0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,// bottom left
//            0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f,// bottom right
//            0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,// top right
//    };

    //use larger vertices to expand the size of square (coz want to set up the proejction viewing)
    //(like back side being blocked...
    static final float[] squareCoords = {
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,// top left
            1.0f,  1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            -1.0f,  1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,// bottom right
            -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,// top right
    };

    static final short[] indices = {
            0, 1, 2,
            0, 2, 3,
    };

    public Square(ShaderProgram shader) {
        super("square", shader, squareCoords, indices);
    }
}
