package com.zano.core.render;

import com.zano.core.Camera;
import com.zano.core.WindowsManager;
import com.zano.core.entity.Entity;
import com.zano.core.entity.Model;
import com.zano.core.lighting.SunLight;
import com.zano.core.shader.StaticShader;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    private final List<Entity> entities;
    private final Camera camera;
    private final List<SunLight> sunLights;
    private EntityRenderer entityRenderer;
    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(List<Entity> entities, List<SunLight> sunLights, Camera camera) throws Exception {
        this.entities = entities;
        this.sunLights = sunLights;
        this.camera = camera;
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer(camera, sunLights, entities);
        skyboxRenderer = new SkyboxRenderer(camera);
    }

    public void render() {
        clear();
        entityRenderer.render();
        skyboxRenderer.render();
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp() {
        entityRenderer.cleanUp();
        skyboxRenderer.cleanUp();
    }


}
