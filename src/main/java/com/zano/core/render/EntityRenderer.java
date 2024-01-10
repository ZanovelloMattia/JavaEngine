package com.zano.core.render;

import com.zano.core.Camera;
import com.zano.core.WindowsManager;
import com.zano.core.entity.Entity;
import com.zano.core.entity.Material;
import com.zano.core.entity.Model;
import com.zano.core.lighting.SunLight;
import com.zano.core.shader.StaticShader;
import com.zano.core.utils.Consts;
import com.zano.core.utils.Transformation;
import com.zano.test.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements IRenderer{

    private final StaticShader shader;
    private final WindowsManager window;
    private final Map<Model, List<Entity>> entities;
    private final List<Entity> entityList;
    private final Camera camera;
    private final List<SunLight> sunLights;

    public EntityRenderer(Camera camera, List<SunLight> sunLights, List<Entity> entityList) throws Exception {
        this.shader = new StaticShader();
        this.window = Launcher.getWindow();
        this.entities = new HashMap<>();
        this.camera = camera;
        this.sunLights = sunLights;
        this.entityList = entityList;
    }

    @Override
    public void bind(Model model) {
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void renderLight(List<SunLight> sunLights){
        shader.setUniform("sunlight", Consts.MAX_LIGHTS, sunLights);
        for(int i = 0; i < Consts.MAX_LIGHTS; i++) {
            if(i < sunLights.size()) {
                shader.setUniform("lightPosition" + "[" + i + "]", sunLights.get(i).getDirection());
            } else {
                shader.setUniform("lightPosition" + "[" + i + "]", 0);
            }
        }
    }

    public void prepare(Entity entity, Camera camera){
        shader.setUniform("textureSampler", 0);
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        entity.updateTransformMatrixes();
        shader.setUniform("transformationMatrix", entity.getTransformMatrixe());
    }

    public void renderMaterial(Material material){
        shader.setUniform("material", material);
    }

    @Override
    public void render() {
        processEntities();
        shader.bind();
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        renderLight(sunLights);


        for(Model model : entities.keySet()){
            bind(model);
            List<Entity> entityList = entities.get(model);
            for(Entity entity : entityList){
                prepare(entity, camera);
                renderMaterial(entity.getModel().getMaterial());

                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();
        }
        shader.unbind();
        entities.clear();
    }

    public void processEntities() {
        for(Entity entity : entityList){
            processEntity(entity);
        }
    }

    private void processEntity(Entity entity) {
        List<Entity> entityList = entities.get(entity.getModel());
        if (entityList != null){
            entityList.add(entity);
        }
        else{
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entity.getModel(), newEntityList);
        }
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
    }
}
