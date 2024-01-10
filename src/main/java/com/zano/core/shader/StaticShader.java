package com.zano.core.shader;

import com.zano.core.entity.Material;
import com.zano.core.entity.Model;
import com.zano.core.lighting.SunLight;
import com.zano.core.utils.Consts;
import org.joml.Vector3f;

import java.util.List;

public class StaticShader extends AbShader{
    private static final String vertexShaderPath = "/resources/shaders/vertex.vs";
    private static final String fragmentShaderPath = "/resources/shaders/fragment.fs";

    public StaticShader() throws Exception {
        super(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void createUniforms() throws Exception {
        super.createUniform("textureSampler");
        super.createUniform("transformationMatrix");
        super.createUniform("projectionMatrix");
        super.createUniform("viewMatrix");
        createSunLightUniform("sunlight", Consts.MAX_LIGHTS);
        createMaterialUniform("material");
        super.createUniform("lightPosition", Consts.MAX_LIGHTS);
    }

    private void createSunLightUniform(String name, int nLight) throws Exception {
        for(int i = 0; i < nLight; i++) {
            super.createUniform(name + "[" + i + "]" + ".colour");
            //super.createUniform(uniformName + "[" + i + "]" + ".pos");
            super.createUniform(name + "[" + i + "]" + ".intensity");
        }
    }

    private void createMaterialUniform(String name) throws Exception {
        super.createUniform(name + ".ambient");
        super.createUniform(name + ".diffuse");
        super.createUniform(name + ".specular");
        super.createUniform(name + ".haveTexture");
        super.createUniform(name + ".reflectance");
        super.createUniform(name + ".reflectancePow");
    }

    public void setUniform(String name, Material material){
        super.setUniform(name + ".ambient", material.getAmbientColour());
        super.setUniform(name + ".diffuse", material.getDiffuseColour());
        super.setUniform(name + ".specular", material.getSpecularColour());
        super.setUniform(name + ".haveTexture", material.hasTexture() ? 1 : 0);
        super.setUniform(name + ".reflectance", material.getReflectance());
        super.setUniform(name + ".reflectancePow", material.getReflectancePow());

    }

    public void setUniform(String name, int nLight, List<SunLight> sunLights){
        for(int i = 0; i < nLight; i++) {
            if(i < sunLights.size()) {
                super.setUniform(name + "[" + i + "]" + ".colour", sunLights.get(i).getColour());
                //setUniform(uniformName + ".pos", sunLight.getDirection());
                super.setUniform(name + "[" + i + "]" + ".intensity", sunLights.get(i).getIntensity());
            }
            else{
                super.setUniform(name + "[" + i + "]" + ".colour", new Vector3f(0,0,0));
                super.setUniform(name + "[" + i + "]" + ".intensity", new Vector3f(0,0,0));
            }
        }
    }
}
