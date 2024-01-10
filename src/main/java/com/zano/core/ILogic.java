package com.zano.core;

public interface ILogic {

    void init() throws Exception;

    void input();

    void update(MouseInput mouseInput);

    void render() throws Exception;

    void cleanup();

}
