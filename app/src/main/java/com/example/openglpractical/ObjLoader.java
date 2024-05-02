package com.example.openglpractical;//package com.example.openglpractical;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjLoader {
    private List<Float> vertices = new ArrayList<>();
    private List<float[]> verticesArrayList = new ArrayList<>();
    private List<float[]> texCoordsArrayList = new ArrayList<>();
    private List<float[]> normalsArrayList = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Map<String, Material> materials = new HashMap<>();

    private static Context context;

    public ObjLoader(Context context, String objFilePath, String mtlFilePath) throws IOException {
        System.out.println("in ObjLoader");
        this.context = context;
        loadObjFile(objFilePath);
        loadMtlFile(mtlFilePath);

    }


    private void loadMtlFile(String mtlFilePath) throws IOException {
        System.out.println("in ObjLoader loading mtl file");

        AssetManager am = context.getAssets();
        InputStream is = am.open(mtlFilePath);

        InputStreamReader isr = new InputStreamReader(is);
        // Wrap the InputStreamReader with a BufferedReader for efficient reading
        BufferedReader reader = new BufferedReader(isr);
        Material currentMaterial = null;
        //System.out.println(reader);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            //System.out.println(line);

            // Skip comments and empty lines
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split("\\s+");

            if (parts[0].equals("newmtl")) {
                String materialName = parts[1];
                currentMaterial = new Material(materialName);
                materials.put(materialName, currentMaterial);
            } else if (currentMaterial != null) {

                if (parts[0].equals("Ns")) {
                    currentMaterial.setShininess(Float.parseFloat(parts[1]));
                } else if (parts[0].equals("Ka")) {
                    currentMaterial.setAmbientColor(
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]));
                } else if (parts[0].equals("Kd")) {
                    currentMaterial.setDiffuseColor(
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]));
                } else if (parts[0].equals("Ks")) {
                    currentMaterial.setSpecularColor(
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]));
                } else if (parts[0].equals("Ni")) {
                    currentMaterial.setRefractiveIndex(Float.parseFloat(parts[1]));
                } else if (parts[0].equals("d")) {
                    currentMaterial.setDissolve(Float.parseFloat(parts[1]));
                } else if (parts[0].equals("illum")) {
                    currentMaterial.setIlluminationModel(Integer.parseInt(parts[1]));
                } else if (parts[0].equals("map_Kd")) {
                    // Handle texture map file (if needed)
                }
                // Add more properties as needed
            }
        }
        System.out.println("finish mtl loader");

        reader.close();

    }

    private void loadObjFile(String objFilePath) throws IOException {
        System.out.println("in ObjLoader loading obj file");

        AssetManager am = context.getAssets();
        InputStream is = am.open(objFilePath);

        InputStreamReader isr = new InputStreamReader(is);
        // Wrap the InputStreamReader with a BufferedReader for efficient reading
        BufferedReader reader = new BufferedReader(isr);
        //System.out.println(reader);

        //load the .mtl file for color //to be done
        Material currentMaterial = null;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            //System.out.println(line);

            String[] parts = line.split("\\s+");

            if (parts.length == 0 || parts[0].startsWith("#")) {
                continue; // Skip comments and empty lines
            }

            if (parts[0].equals("v")) {
                float x = Float.parseFloat(parts[1]);
                float y = Float.parseFloat(parts[2]);
                float z = Float.parseFloat(parts[3]);

                // Create an array to hold the vertex coordinates
                float[] vertex = {x, y, z};

                // Add the array to the ArrayList
                verticesArrayList.add(vertex);

            } else if (parts[0].equals("vt")) {
                float u = Float.parseFloat(parts[1]);
                float v = Float.parseFloat(parts[2]);

                // Create an array to hold the texture coordinates
                float[] texture = {u, v};

                // Add the array to the ArrayList
                texCoordsArrayList.add(texture);

            } else if (parts[0].equals("vn")) {
                float nx = Float.parseFloat(parts[1]);
                float ny = Float.parseFloat(parts[2]);
                float nz = Float.parseFloat(parts[3]);

                // Create an array to hold the normal coordinates
                float[] normal = {nx, ny, nz};

                // Add the array to the ArrayList
                normalsArrayList.add(normal);

            } else if (parts[0].equals("usemtl")) {
                String materialName = parts[1];
                currentMaterial = materials.get(materialName);
            } else if (parts[0].equals("f")) {
                for (int i = 1; i < parts.length; i++) {
                    String[] vertexData = parts[i].split("/");
                    int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
                    indices.add(vertexIndex);
                    float[] vertex = verticesArrayList.get(vertexIndex);
                    System.out.println(vertexData.length + " " + verticesArrayList.get(vertexIndex).length);

                    if (vertexData.length == 3 && verticesArrayList.get(vertexIndex).length < 8) {

                        int texCoordIndex = Integer.parseInt(vertexData[1]) - 1;
                        float[] texCoord = texCoordsArrayList.get(texCoordIndex);

                        int normalIndex = Integer.parseInt(vertexData[2]) - 1;
                        float[] normal = normalsArrayList.get(normalIndex);

                        // Create a new array to hold the combined vertex, texture coordinate, and normal
                        float[] combinedVertex = new float[vertex.length + texCoord.length + normal.length];

                        // Copy the elements from vertex array
                        System.arraycopy(vertex, 0, combinedVertex, 0, vertex.length);

                        // Copy the elements from texCoord array
                        System.arraycopy(texCoord, 0, combinedVertex, vertex.length, texCoord.length);

                        // Copy the elements from normal array
                        System.arraycopy(normal, 0, combinedVertex, vertex.length + texCoord.length, normal.length);

                        // Update the vertex array in the verticesArrayList
                        verticesArrayList.set(vertexIndex, combinedVertex);

                    }
                    else if(vertexData.length == 1 && verticesArrayList.get(vertexIndex).length < 8){

                        int texCoordIndex = vertexIndex;
                        float[] texCoord = texCoordsArrayList.get(texCoordIndex);

                        int normalIndex = vertexIndex;
                        float[] normal = normalsArrayList.get(normalIndex);

                        // Create a new array to hold the combined vertex, texture coordinate, and normal
                        float[] combinedVertex = new float[vertex.length + texCoord.length + normal.length];

                        // Copy the elements from vertex array
                        System.arraycopy(vertex, 0, combinedVertex, 0, vertex.length);

                        // Copy the elements from texCoord array
                        System.arraycopy(texCoord, 0, combinedVertex, vertex.length, texCoord.length);

                        // Copy the elements from normal array
                        System.arraycopy(normal, 0, combinedVertex, vertex.length + texCoord.length, normal.length);

                        // Update the vertex array in the verticesArrayList
                        verticesArrayList.set(vertexIndex, combinedVertex);

                    }
                }
            }
        }

//        for(float[] vertices : verticesArrayList){
//            for(float vertex : vertices){
//                System.out.print(vertex + " ");
//            }
//            System.out.println(); // Move to the next line after printing all elements of the array
//        }


        // Calculate the total length needed for the vertices array
        int totalLength = 0;
        for (float[] array : verticesArrayList) {
            totalLength += array.length;
        }

        // Create the vertices array with the calculated length
        float[] verticesArray = new float[totalLength];

        // Copy the elements of each array in verticesArrayList into the vertices array
        int currentIndex = 0;
        for (float[] array : verticesArrayList) {
            System.arraycopy(array, 0, verticesArray, currentIndex, array.length);
            currentIndex += array.length;
        }

        // Iterate over the verticesArray and add each element to the vertices ArrayList
        for (float vertex : verticesArray) {
            vertices.add(vertex);
        }

        System.out.println("finish obj loader");

        reader.close();
    }

    // Define Material class as needed to store material properties
    static class Material {
        public String name;
        public float[] ambientColor = { 0.0f, 0.0f, 0.0f };
        public float[] diffuseColor = { 1.0f, 1.0f, 1.0f };
        public float[] specularColor = { 1.0f, 1.0f, 1.0f };
        public float shininess = 0.0f;
        public float refractiveIndex = 1.0f;
        public float dissolve = 1.0f;
        public int illuminationModel = 0;

        public Material(String name) {
            this.name = name;
        }

        public void setAmbientColor(float r, float g, float b) {
            ambientColor[0] = r;
            ambientColor[1] = g;
            ambientColor[2] = b;
        }

        public void setDiffuseColor(float r, float g, float b) {
            diffuseColor[0] = r;
            diffuseColor[1] = g;
            diffuseColor[2] = b;
        }

        public void setSpecularColor(float r, float g, float b) {
            specularColor[0] = r;
            specularColor[1] = g;
            specularColor[2] = b;
        }

        public void setShininess(float shininess) {
            this.shininess = shininess;
        }

        public void setRefractiveIndex(float refractiveIndex) {
            this.refractiveIndex = refractiveIndex;
        }

        public void setDissolve(float dissolve) {
            this.dissolve = dissolve;
        }

        public void setIlluminationModel(int illuminationModel) {
            this.illuminationModel = illuminationModel;
        }

        // Add getters as needed
    }

    // Getter methods for accessing loaded data
    public List<Float> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public Map<String, Material> getMaterials() {
        return materials;
    }

//    public static void main(String[] args) {
//        try {
//            System.out.println("in oObjLoader main");
//
//            ObjLoader objLoader = new ObjLoader(context, "tree.obj", "tree.mtl");
//            List<Float> vertices = objLoader.getVertices();
//            List<Float> texCoords = objLoader.getTexCoords();
//            List<Float> normals = objLoader.getNormals();
//            List<Integer> indices = objLoader.getIndices();
//            Map<String, Material> materials = objLoader.getMaterials();
//
//            // Use the loaded data as needed
//            System.out.println("Vertices: " + vertices);
//            System.out.println("TexCoords: " + texCoords);
//            System.out.println("Normals: " + normals);
//            System.out.println("Indices: " + indices);
//            System.out.println("Materials: " + materials);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

