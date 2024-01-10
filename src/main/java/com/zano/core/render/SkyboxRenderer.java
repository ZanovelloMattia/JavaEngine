package com.zano.core.render;

import com.zano.core.Camera;
import com.zano.core.ObjectLoader;
import com.zano.core.WindowsManager;
import com.zano.core.entity.Model;
import com.zano.core.entity.Texture;
import com.zano.core.shader.SkyboxShader;
import com.zano.core.utils.Transformation;
import com.zano.test.Launcher;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyboxRenderer implements IRenderer{

    private final WindowsManager window;
    private final Camera camera;
    private final SkyboxShader shader;

    private final float SIZE = 500f;

    private final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private String[] TEXTURE_FILES = {"/resources/textures/SkyBox/positiveX.png",
            "/resources/textures/SkyBox/negativeX.png",
            "/resources/textures/SkyBox/positiveY.png",
            "/resources/textures/SkyBox/negativeY.png",
            "/resources/textures/SkyBox/positiveZ.png",
            "/resources/textures/SkyBox/negativeZ.png"};

    private Model cube;
    private ObjectLoader loader;

    public SkyboxRenderer(Camera camera) throws Exception {
        this.window = Launcher.getWindow();
        this.camera = camera;

        loader = new ObjectLoader();
        cube = loader.loadModel(VERTICES);
        cube.setTexture(new Texture(loader.loadTextureCube(TEXTURE_FILES)));
        shader = new SkyboxShader();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, model.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void render() {
        shader.bind();
        shader.setUniform("projectionMatrix", window.getProjectionMatrix());
        Matrix4f viewMatrix = Transformation.getViewMatrix(camera);
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        shader.setUniform("viewMatrix", viewMatrix);
        bind(cube);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        unbind();
        shader.unbind();
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
    }
}
