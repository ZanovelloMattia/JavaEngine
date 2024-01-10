package com.zano.core.render;


import com.zano.core.ObjectLoader;
import com.zano.core.entity.Model;

public interface IRenderer{
    void bind(Model model);
    void unbind();
    void render();
    void cleanUp();

}
