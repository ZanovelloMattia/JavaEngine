package com.zano.core;

import com.zano.core.entity.Model;
import com.zano.core.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
//import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
//import org.lwjgl.stb.STBImage;
//import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

//import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class ObjectLoader {
    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    public Model loadOBJModel(String filename){
        List<String> lines = Utils.readAllLine(filename);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for(String line : lines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){
                case "v":
                    //vertices
                    Vector3f verticesVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    //vertex textures
                    Vector2f textureVec = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(textureVec);
                    break;
                case "vn":
                    //vertex normals
                    Vector3f normalVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normalVec);
                    break;
                case "f":
                    //faces
                    processFace(faces, tokens[1], tokens[2], tokens[3]);
                    break;
                default:
                    break;
            }
        }


        float[] verticesArr = new float[faces.size() * 3];
        float[] texCoordArr = new float[faces.size() * 2];
        float[] normalArr = new float[faces.size() * 3];




        List<Integer> indices = new ArrayList<>();

        int i = 0;
        for (Vector3i face : faces){
            processVertex(face, vertices, textures, normals, indices, verticesArr, texCoordArr, normalArr, i);
            i++;
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticesArr, texCoordArr, normalArr,indicesArr);
    }

    private static void processVertex(Vector3i face, List<Vector3f> vertices, List<Vector2f> texCoordList,
                                      List<Vector3f> normalList, List<Integer> indicesList,
                                      float[] verticesArr, float[] texCoordArr, float[] normalArr, int index){
        indicesList.add(index);

        verticesArr[index * 3] = vertices.get(face.x).x;
        verticesArr[index * 3 + 1] = vertices.get(face.x).y;
        verticesArr[index * 3 + 2] = vertices.get(face.x).z;

        texCoordArr[index * 2] = texCoordList.get(face.y).x;
        texCoordArr[index * 2 + 1] = texCoordList.get(face.y).y;

        normalArr[index * 3] = normalList.get(face.z).x;
        normalArr[index * 3 + 1] = normalList.get(face.z).y;
        normalArr[index * 3 + 2] = normalList.get(face.z).z;
    }

    private static void processFace(List<Vector3i> faces, String... vert){
        for(int i = 0; i < vert.length; i++) {
            String[] tokens = vert[i].split("/");
            int length = tokens.length;

            int pos, coords = -1, normal = -1;
            pos = Integer.parseInt(tokens[0]) - 1;
            if(length > 1) {
                String textCord = tokens[1];
                coords = !textCord.isEmpty() ? Integer.parseInt(textCord) - 1 : -1;
                if(length > 2)
                    normal = Integer.parseInt(tokens[2]) - 1;
            }
            Vector3i facesVec = new Vector3i(pos, coords, normal);
            faces.add(facesVec);
        }
    }

    public Model loadModel(List<Vector3f> vertices,List<Vector2f> textures, List<Vector3f> normals, List<Vector3i> faces){

        float[] verticesArr = new float[faces.size() * 3];
        float[] texCoordArr = new float[faces.size() * 2];
        float[] normalArr = new float[faces.size() * 3];

        List<Integer> indices = new ArrayList<>();

        int i = 0;
        for (Vector3i face : faces){
            processVertex(face, vertices, textures, normals, indices, verticesArr, texCoordArr, normalArr, i);
            i++;
        }
        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticesArr, texCoordArr, normalArr,indicesArr);
    }

    public Model loadModel(List<Vector3f> vertices,List<Vector2f> textures, List<Vector3f> normals){

        float[] verticesArr = new float[vertices.size() * 3];
        float[] texCoordArr = new float[vertices.size() * 2];
        float[] normalArr = new float[vertices.size() * 3];
        int[] indicesArr = new int[vertices.size()];

        for(int i = 0; i < vertices.size(); i++) {
            verticesArr[i * 3] = vertices.get(i).x;
            verticesArr[i * 3 + 1] = vertices.get(i).y;
            verticesArr[i * 3 + 2] = vertices.get(i).z;

            texCoordArr[i * 2] = textures.get(i).x;
            texCoordArr[i * 2 + 1] = textures.get(i).y;

            normalArr[i * 3] = normals.get(i).x;
            normalArr[i * 3 + 1] = normals.get(i).y;
            normalArr[i * 3 + 2] = normals.get(i).z;

            indicesArr[i] = i;
        }

        return loadModel(verticesArr, texCoordArr, normalArr,indicesArr);
    }

    public Model loadModel(float[] vertices,float[] textureCoords, float[] normals, int[] indices){
        int id = createVAO();
        storeIndicesBudder(indices);
        storeDataInAtribList(0, 3, vertices);
        storeDataInAtribList(1,2,textureCoords);
        storeDataInAtribList(2,3,normals);
        unbind();
        return new Model(id, indices.length);
    }

    public Model loadModel(float[] vertices){
        int id = createVAO();
        storeDataInAtribList(0, 3, vertices);
        unbind();
        return new Model(id, vertices.length);
    }

    public int loadTexture(String filename) throws Exception{
        /*int width, height;
        ByteBuffer buffer;

        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if(buffer == null)
                throw  new Exception("Image File " + filename + " not loaded" + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

        }
        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;*/

        int[] pixels = null;
        int width = 0;
        int height = 0;
        try (InputStream in = Utils.class.getResourceAsStream(filename)){
            BufferedImage image = ImageIO.read(Objects.requireNonNull(in));
            if(image == null)
                throw  new Exception("Image File " + filename + " not loaded");
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        textures.add(result);
        return result;

    }

    public int loadTextureCube(String[] filename) throws Exception{

        int result = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, result);
        for(int j = 0; j < filename.length; j++) {
            int[] pixels = null;
            int width = 0;
            int height = 0;
            try (InputStream in = Utils.class.getResourceAsStream(filename[j])) {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(in));
                if (image == null)
                    throw new Exception("Image File " + Arrays.toString(filename) + " not loaded");
                width = image.getWidth();
                height = image.getHeight();
                pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int[] data = new int[width * height];
            for (int i = 0; i < width * height; i++) {
                int a = (pixels[i] & 0xff000000) >> 24;
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);

                data[i] = a << 24 | b << 16 | g << 8 | r;
            }
            IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
            buffer.put(data).flip();
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        textures.add(result);
        return result;
    }

    private int createVAO(){
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBudder(int[] indices){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAtribList(int attribNo, int vertexCount, float[] data){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
    }

    private void unbind(){
        GL30.glBindVertexArray(0);
    }

    public void cleanup(){
        for (int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            GL30.glDeleteVertexArrays(vbo);
        for (int texture : textures)
            GL11.glDeleteTextures(texture);
    }
}
