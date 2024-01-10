package com.zano.core.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Consts {

    public static final String TITLE = "Zano";

    public static final int MAX_LIGHTS = 3;

    public static final float FOW = (float)  Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    public static final float CAMERA_MOVE_SPEED = 0.05f;
    public  static final float MOUSE_SENSITIVITY = 0.2f;

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    public static final float AMBIENT_LIGHT = 0.2f;

    public static final float SPECULAR_POWER = 10f;
    public static final float reflectivity = 1f;
}
