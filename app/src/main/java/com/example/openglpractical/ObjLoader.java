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
    private List<Float> texCoords = new ArrayList<>();
    private List<Float> normals = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Map<String, Material> materials = new HashMap<>();

    private static Context context;

    public ObjLoader(Context context, String objFilePath, String mtlFilePath) throws IOException {
        System.out.println("in ObjLaader");
        this.context = context;
        loadObjFile(objFilePath);
        loadMtlFile(mtlFilePath);

    }


    private void loadMtlFile(String mtlFilePath) throws IOException {
        System.out.println("in oObjLoader loadingmtlfile");

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

                String[] parts = line.split("\\s+");

                if (parts.length == 0 || parts[0].startsWith("#")) {
                    continue; // Skip comments and empty lines
                }

                if (parts[0].equals("newmtl")) {
                    String materialName = parts[1];
                    currentMaterial = new Material(materialName);
                    materials.put(materialName, currentMaterial);
                } else if (currentMaterial != null) {
                    if (parts[0].equals("Ka")) {
                        currentMaterial.setAmbientColor(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3]));
                    } else if (parts[0].equals("Kd")) {
                        currentMaterial.setDiffuseColor(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3]));
                    } else if (parts[0].equals("Ks")) {
                        currentMaterial.setSpecularColor(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3]));
                    }// Add more properties as needed
                }
            }
        System.out.println("finish mtl loader");

            reader.close();

    }

    private void loadObjFile(String objFilePath) throws IOException {
        System.out.println("in oObjLoader loading obj file");

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
                vertices.add(x);
                vertices.add(y);
                vertices.add(z);
            } else if (parts[0].equals("vt")) {
                float u = Float.parseFloat(parts[1]);
                float v = Float.parseFloat(parts[2]);
                texCoords.add(u);
                texCoords.add(v);
            } else if (parts[0].equals("vn")) {
                float nx = Float.parseFloat(parts[1]);
                float ny = Float.parseFloat(parts[2]);
                float nz = Float.parseFloat(parts[3]);
                normals.add(nx);
                normals.add(ny);
                normals.add(nz);
            } else if (parts[0].equals("usemtl")) {
                String materialName = parts[1];
                currentMaterial = materials.get(materialName);
            } else if (parts[0].equals("f")) {
                for (int i = 1; i < parts.length; i++) {
                    String[] vertexData = parts[i].split("/");
                    int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
                    indices.add(vertexIndex);

                    if (vertexData.length >= 2) {
                        int texCoordIndex = Integer.parseInt(vertexData[1]) - 1;
                        // You can use texture coordinates here if needed
                    }

                    if (vertexData.length >= 3) {
                        int normalIndex = Integer.parseInt(vertexData[2]) - 1;
                        // You can use normals here if needed
                    }
                }
            }
        }
        System.out.println("finish obj loader");

        reader.close();
    }

    // Define Material class as needed to store material properties
    static class Material {
        private String name;
        private float[] ambientColor = { 0.0f, 0.0f, 0.0f };
        private float[] diffuseColor = { 1.0f, 1.0f, 1.0f };
        private float[] specularColor = { 1.0f, 1.0f, 1.0f };

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

        // Add getters as needed
    }

    // Getter methods for accessing loaded data
    public List<Float> getVertices() {
        return vertices;
    }

    public List<Float> getTexCoords() {
        return texCoords;
    }

    public List<Float> getNormals() {
        return normals;
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

