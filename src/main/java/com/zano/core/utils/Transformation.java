package com.zano.core.utils;

import com.zano.core.Camera;
import com.zano.core.entity.Entity;
import org.joml.*;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Vector3f pos, Vector3f rot, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(pos.x, pos.y, pos.z).
                rotateX((float) Math.toRadians(rot.x)).
                rotateY((float) Math.toRadians(rot.y)).
                rotateZ((float) Math.toRadians(rot.z)).
                translate(pos.x, pos.y, pos.z).
                scale(scale);
        return matrix;
    }

    public static Matrix4f mulMatrix4f(List<Matrix4f> matrixs) {
        Matrix4f finalMatrix = new Matrix4f();
        finalMatrix = matrixs.get(0);
        for(int i = 1; i < matrixs.size(); i++){
            finalMatrix.mul(matrixs.get(i));
        }
        return finalMatrix;
    }

    /*


if(entity.getRotOfset().x == 0 && entity.getRotOfset().y == 0 && entity.getRotOfset().z == 0) {
            matrix.identity().translate(entity.getPos().x, entity.getPos().y, entity.getPos().z + 1).
                    translate(entity.getRotOfset().x, entity.getRotOfset().y, entity.getRotOfset().z).
                    rotateX((float) Math.toRadians(entity.getRotation().x)).
                    rotateY((float) Math.toRadians(entity.getRotation().y)).
                    rotateZ((float) Math.toRadians(entity.getRotation().z)).
                    translate(-entity.getRotOfset().x, -entity.getRotOfset().y, -entity.getRotOfset().z).
                    translate(entity.getPos().x, entity.getPos().y, entity.getPos().z - 1).
                    scale(entity.getScale());
        }else {
            matrix.identity().translate(entity.getPos().x, entity.getPos().y, entity.getPos().z + 1).
                    translate(entity.getRotOfset().x, entity.getRotOfset().y, entity.getRotOfset().z).
                    rotateAround(vectorToQaut(entity.getRotation()), entity.getRotOfset().x, entity.getRotOfset().y, entity.getRotOfset().z).
                    translate(-entity.getRotOfset().x, -entity.getRotOfset().y, -entity.getRotOfset().z).
                    translate(entity.getPos().x, entity.getPos().y, entity.getPos().z - 1).
                    scale(entity.getScale());
        }
     */


    public static Quaternionf vectorToQaut(Vector3f vector){
        float x = (float) Math.toRadians(vector.x);
        float y = (float) Math.toRadians(vector.y);
        float z = (float) Math.toRadians(vector.z);

        float qw = (float) (Math.cos(x/2)*Math.cos(y/2)*Math.cos(z/2) +
                Math.sin(x/2)*Math.sin(y/2)*Math.sin(z/2));
        float qx = (float) (Math.sin(x/2)*Math.cos(y/2)*Math.cos(z/2) -
                Math.cos(x/2)*Math.sin(y/2)*Math.sin(z/2));
        float qy = (float) (Math.cos(x/2)*Math.sin(y/2)*Math.cos(z/2) +
                Math.sin(x/2)*Math.cos(y/2)*Math.sin(z/2));
        float qz = (float) (Math.cos(x/2)*Math.cos(y/2)*Math.sin(z/2) +
                Math.sin(x/2)*Math.sin(y/2)*Math.cos(z/2));
        return new Quaternionf(qx, qy, qz, qw);
    }

    /*public static Vector3f RmatrixToVectr(Matrix4f matrix){
        float sx = (float) Math.pow((matrix.m00()*matrix.m00() + matrix.m10()*matrix.m10() + matrix.m20()*matrix.m20()), (double) 1/3);
        float sy = (float) Math.pow((matrix.m01()*matrix.m01() + matrix.m11()*matrix.m11() + matrix.m21()*matrix.m21()), (double) 1/3);
        float sz = (float) Math.pow((matrix.m02()*matrix.m02() + matrix.m12()*matrix.m12() + matrix.m22()*matrix.m22()), (double) 1/3);
    }*/
/*
matrix.identity().translate(entity.getPos().x, entity.getPos().y, entity.getPos().z + 1).
                    translate(entity.getRotOfset().x, entity.getRotOfset().y, entity.getRotOfset().z).
                    rotateX((float) Math.toRadians(entity.getRotation().x)).
                    rotateY((float) Math.toRadians(entity.getRotation().y)).
                    rotateZ((float) Math.toRadians(entity.getRotation().z)).
                    translate(-entity.getRotOfset().x, -entity.getRotOfset().y, -entity.getRotOfset().z).
                    translate(entity.getPos().x, entity.getPos().y, entity.getPos().z - 1).
                    scale(entity.getScale());
 */
    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0)).
                rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0)).
                rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }
}
