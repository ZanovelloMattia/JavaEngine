package com.zano.test;

import com.zano.core.*;
import com.zano.core.entity.Entity;
import com.zano.core.entity.Model;
import com.zano.core.entity.Texture;
import com.zano.core.lighting.SunLight;
import com.zano.core.render.MasterRenderer;
import com.zano.core.terrains.Terrain;
import com.zano.core.utils.Consts;
import com.zano.core.utils.ModelCreator;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TestGame implements ILogic {
    private final MasterRenderer renderer;
    private final ObjectLoader loader;
    private final WindowsManager window;

    private final List<Entity> entities;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private List<SunLight> sunLights;

    private Terrain terrain;

    public TestGame() throws Exception {
        entities = new ArrayList<>();
        sunLights = new ArrayList<>();
        camera = new Camera();
        renderer = new MasterRenderer(entities, sunLights, camera);
        window = Launcher.getWindow();
        loader = new ObjectLoader();

        cameraInc = new Vector3f(0,0,0);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model terrainModel = new Terrain(0,0, loader).getModel();
        terrainModel.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 1f);

        Model map = loader.loadOBJModel("/resources/models/cube.obj");
        //map.setTexture(new Texture(loader.loadTexture("/resources/textures/verdeAcqua.png")), 0.2f);

        Model piece = loader.loadOBJModel("/resources/models/cube1.obj");
        piece.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 0.2f);

        Model piece2 = loader.loadOBJModel("/resources/models/cubeSample.obj");
        piece2.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 0.2f);

        Model piece3 = loader.loadOBJModel("/resources/models/cubeSample.obj");
        piece3.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 0.2f);

        Model piece4 = ModelCreator.createRect(2, 1.5f, 1);
        piece4.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 1.2f);

        Model piece5 = ModelCreator.createRect(2.3f);
        piece5.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 1.2f);

        Model piece6 = ModelCreator.createSphere(1, 20, 20);
        piece6.setTexture(new Texture(loader.loadTexture("/resources/textures/white.png")), 1.2f);

        entities.add(new Entity(terrainModel, new Vector3f(200, 0, 200), new Vector3f(0,0,0), 1));
        entities.add(new Entity(map, new Vector3f(0, 0.2f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece, new Vector3f(0, 0.4f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece2, new Vector3f(2, 1f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece3, new Vector3f(10, 10f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece4, new Vector3f(0, 2f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece5, new Vector3f(0, 10f, 0), new Vector3f(0, 0, 0), 1));
        entities.add(new Entity(piece6, new Vector3f(0, 5f, 0), new Vector3f(0, 0, 0), 1));


        sunLights.add(new SunLight(new Vector3f(1f, 0.9f, 1f), new Vector3f(100, 100, 100), 0.5f));
        sunLights.add(new SunLight(new Vector3f(1f, 1f, 0.9f), new Vector3f(-100, 100, -100), 0.2f));
        sunLights.add(new SunLight(new Vector3f(0.9f, 1f, 1f), new Vector3f(-100, 100, 100), 0.3f));
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_Q))
            cameraInc.y = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_E))
            cameraInc.y = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            entities.get(2).incPos(0, 0, 0.01f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)){
            entities.get(2).incPos(0, 0, -0.01f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
            entities.get(2).incPos(0.01f, 0, 0);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)){
            entities.get(2).incPos(-0.01f, 0, 0);
        }
    }

    @Override
    public void update(MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if(mouseInput.isRightButtonPress()){
            Vector2f rotVex = mouseInput.getDisplVec();
            camera.moveRotation(rotVex.x * Consts.MOUSE_SENSITIVITY, rotVex.y * Consts.MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render() throws Exception{
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(false);
        }

        renderer.render();
    }

    @Override
    public void cleanup() {
        renderer.cleanUp();
        loader.cleanup();
    }
}
