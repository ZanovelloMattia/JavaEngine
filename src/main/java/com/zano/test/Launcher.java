package com.zano.test;

import com.zano.core.EngineManager;
import com.zano.core.WindowsManager;
import com.zano.core.utils.Consts;

public class Launcher {

    private static WindowsManager window;
    private static TestGame game;

    public static void main(String[] args) throws Exception {
        window = new WindowsManager(Consts.TITLE, 1600, 900, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowsManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
