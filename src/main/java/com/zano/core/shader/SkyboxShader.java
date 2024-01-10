package com.zano.core.shader;

public class SkyboxShader extends AbShader{

    private static final String vertexShaderPath = "/resources/shaders/skyboxVertexShader.vs";
    private static final String fragmentShaderPath = "/resources/shaders/skyboxFragmentShader.fs";

    public SkyboxShader() throws Exception {
        super(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void createUniforms() throws Exception {
        super.createUniform("projectionMatrix");
        super.createUniform("viewMatrix");
    }
}
