package com.zano.core.shader;

import com.zano.core.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbShader {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public AbShader(String vertexShaderPath, String fragmentShaderPath) throws Exception {
        programID = GL20.glCreateProgram();
        if (programID == 0) {
            throw new Exception("Could not create shader");
        }

        vertexShaderID = createShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = createShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);

        uniforms = new HashMap<>();

        bindAttributes();
        linkShader();
        createUniforms();
    }


    //--------------------------------------------------------------------------------------------------
    //Shader section

    //Create shader bast on the file patch and shader type
    private int createShader(String filePath, int shaderType) throws Exception {
        int shaderID = GL20.glCreateShader(shaderType);
        if (shaderID == 0) {
            throw new Exception("Error creating shader. Type : " + shaderType);
        }

        String shaderCode = Utils.loadResource(filePath);

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: TYPE: " + shaderType
                    + " Info " + GL20.glGetShaderInfoLog(shaderID, 1024));
        }

        GL20.glAttachShader(programID, shaderID);

        return shaderID;
    }

    //Link and validate shader
    private void linkShader() throws Exception {
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: TYPE: " + " Info " + GL20.glGetProgramInfoLog(programID, 1024));
        }

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Unable to validate shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);

        unbind();

        if (programID != 0) {
            GL20.glDeleteProgram(programID);

        }
    }

    //-------------------------------------------------------------------------------------
    //Attribute section

    protected abstract void bindAttributes();

    protected void bindAttribute(int attrPos, String name) {
        GL20.glBindAttribLocation(programID, attrPos, name);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Uniform section for creation and set

    protected abstract void createUniforms() throws Exception;

    protected void createUniform(String name) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(programID, name);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform " + name);
        }

        uniforms.put(name, uniformLocation);
    }

    public void createUniform(String name, int iteration) throws Exception {
        for (int i = 0; i < iteration; i++) {
            createUniform(name + "[" + i + "]");
        }
    }

    public void setUniform(String name, int iteration, Matrix4f... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(uniforms.get(name), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String name, int iteration, Vector4f... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, Vector4f value) {
        GL20.glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, int iteration, Vector3f... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, Vector3f value) {
        GL20.glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, int iteration, boolean... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, boolean value) {
        float res = 0;
        if (value)
            res = 1;
        GL20.glUniform1f(uniforms.get(name), res);
    }

    public void setUniform(String name, int iteration, int... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(uniforms.get(name), value);
    }

    public void setUniform(String name, int iteration, float... values) {
        for (int i = 0; i < values.length; i++) {
            setUniform(name + "[" + i + "]", values[i]);
        }
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(uniforms.get(name), value);
    }
}
