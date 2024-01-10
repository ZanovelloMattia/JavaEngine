package com.zano.core.utils;

import com.zano.core.ObjectLoader;
import com.zano.core.entity.Model;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelCreator {

    static ObjectLoader loader = new ObjectLoader();
    private static float height = 1f;
    private static float depth = 1f;
    private static float width = 1f;


    //               f              l              r           b               u           d
    // indices == 0,1,2,2,3,0 - 4,1,0,0,5,4 - 2,6,7,7,3,2 - 4,5,7,7,6,4 - 0,3,7,7,5,0 - 4,1,2,2,6,4

    /*private static float[] verticesArr = {
            -width, height, -depth,
            -width, -height, -depth,
            width, -height, -depth,
            width, height, -depth,
            -width, -height, depth,
            -width, height, depth,
            width, -height, depth,
            width, height, depth
    };*/
    private static List<Vector3f> cubeVerticesList;

    private static final Vector3f[] cubenormalsArr = {
            new Vector3f(0.0f, 0.0f, -1.0f),
            new Vector3f(0.0f, 0.0f, 1.0f),
            new Vector3f(0.0f, -1.0f, 0.0f),
            new Vector3f(1.0f, 0.0f, 0.0f),
            new Vector3f( 0.0f, 1.0f, 0.0f),
            new Vector3f( -1.0f, 0.0f, 0.0f)
    };
    private static final List<Vector3f> cubeNormalsList = new ArrayList<>(List.of(cubenormalsArr));

    private static final Vector2f[] cubeTextCoordinatsArr = {
            new Vector2f(1.0f, 0.0f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(0.0f, 1.0f),
            new Vector2f(0.0f, 0.0f)
    };
    private static final List<Vector2f> cubeTextCoordinatsList = new ArrayList<>(List.of(cubeTextCoordinatsArr));

    private static final Vector3i[] cubeFacesArr = {
            new Vector3i(2, 0, 0), new Vector3i(1, 3, 0), new Vector3i(0, 2, 0), new Vector3i(0, 2, 0), new Vector3i(3, 1, 0), new Vector3i(2, 0, 0),
            new Vector3i(0, 1, 5), new Vector3i(1, 0, 5), new Vector3i(4, 3, 5), new Vector3i(4, 3, 5), new Vector3i(5, 2, 5), new Vector3i(0, 1, 5),
            new Vector3i(7, 1, 3), new Vector3i(6, 0, 3), new Vector3i(2, 3, 3), new Vector3i(2, 3, 3), new Vector3i(3, 2, 3), new Vector3i(7, 1, 3),
            new Vector3i(7, 2, 1), new Vector3i(5, 1, 1),new Vector3i(4, 0, 1), new Vector3i(4, 0, 1),new Vector3i(6, 3, 1), new Vector3i(7, 2, 1),
            new Vector3i(7, 1, 4), new Vector3i(3, 0, 4), new Vector3i(0, 3, 4), new Vector3i(0, 3, 4), new Vector3i(5, 2, 4), new Vector3i(7, 1, 4),
            new Vector3i(4, 3, 2), new Vector3i(1, 2, 2), new Vector3i(2, 1, 2), new Vector3i(2, 1, 2), new Vector3i(6, 0, 2), new Vector3i(4, 3, 2)
    };
    private static final List<Vector3i> cubeFacesList = new ArrayList<>(List.of(cubeFacesArr));

    public static Model createRect(float width, float height, float depth) {
        float[] verticesArr = {
                -width, height, -depth,
                -width, -height, -depth,
                width, -height, -depth,
                width, height, -depth,
                -width, -height, depth,
                -width, height, depth,
                width, -height, depth,
                width, height, depth
        };
        updateVerticesList(verticesArr);
        return loader.loadModel(cubeVerticesList, cubeTextCoordinatsList, cubeNormalsList, cubeFacesList);
    }

    public static Model createRect(float size) {
        float[] verticesArr = {
                -size, size, -size,
                -size, -size, -size,
                size, -size, -size,
                size, size, -size,
                -size, -size, size,
                -size, size, size,
                size, -size, size,
                size, size, size
        };
        updateVerticesList(verticesArr);
        return loader.loadModel(cubeVerticesList, cubeTextCoordinatsList, cubeNormalsList, cubeFacesList);
    }

    private static void updateVerticesList(float[] verticesArr){
        cubeVerticesList = new ArrayList<>();
        for(int i = 0; i < verticesArr.length/3; i++) {
            cubeVerticesList.add(new Vector3f(verticesArr[i * 3], verticesArr[i * 3 + 1], verticesArr[i * 3 + 2]));
        }
    }

    private static List<Vector3f> sphereVerticesList;
    private static List<Vector3f> sphereNormalsList;

    private static final Vector2f[] sphereTextCoordinatsArr = {
            new Vector2f(1.0f, 0.0f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(0.0f, 1.0f),
            new Vector2f(0.0f, 0.0f)
    };
    private static List<Vector2f> sphereTextCoordinatsList;
    private static List<Vector3i> sphereFacesList;

    public static Model createSphere(float r, int lats, int longs) {
        sphereVerticesList = new ArrayList<>();
        sphereNormalsList = new ArrayList<>();
        sphereTextCoordinatsList = new ArrayList<>();
        sphereFacesList = new ArrayList<>();

        Random rand = new Random();

        float startU=0;
        float startV=0;
        float endU=(float) Math.PI*2;
        float  endV=(float) Math.PI;
        float stepU=(endU-startU)/longs; // step size between U-points on the grid
        float stepV=(endV-startV)/lats; // step size between V-points on the grid
        for(var i=0;i<longs;i++){ // U-points
            for(var j=0;j<lats;j++){ // V-points
                float u=i*stepU+startU;
                float v=j*stepV+startV;
                float un=(i+1==longs) ? endU : (i+1)*stepU+startU;
                float vn=(j+1==lats) ? endV : (j+1)*stepV+startV;
                // Find the four points of the grid
                // square by evaluating the parametric
                // surface function
                Vector3f p0= clacPoint(u, v, r);
                Vector3f p1= clacPoint(u, vn, r);
                Vector3f p2= clacPoint(un, v, r);
                Vector3f p3= clacPoint(un, vn, r);
                Vector3f pn0= clacPoint(u, v);
                Vector3f pn1= clacPoint(u, vn);
                Vector3f pn2= clacPoint(un, v);
                Vector3f pn3= clacPoint(un, vn);
                // NOTE: For spheres, the normal is just the normalized
                // version of each vertex point; this generally won't be the case for
                // other parametric surfaces.
                // Output the first triangle of this grid square
                sphereVerticesList.add(p0);
                sphereVerticesList.add(p2);
                sphereVerticesList.add(p1);
                sphereNormalsList.add(pn0);
                sphereNormalsList.add(pn2);
                sphereNormalsList.add(pn1);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
                // Output the other triangle of this grid square
                sphereVerticesList.add(p3);
                sphereVerticesList.add(p1);
                sphereVerticesList.add(p2);
                sphereNormalsList.add(pn3);
                sphereNormalsList.add(pn1);
                sphereNormalsList.add(pn2);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
                sphereTextCoordinatsList.add(sphereTextCoordinatsArr[rand.nextInt(3)]);
            }
        }
        return loader.loadModel(sphereVerticesList, sphereTextCoordinatsList, sphereNormalsList);
    }

    private static Vector3f clacPoint(float u, float v, float r) {
        return new Vector3f( (float) (Math.cos(u)*Math.sin(v)*r), (float) Math.cos(v)*r, (float) (Math.sin(u)*Math.sin(v)*r));
    }

    private static Vector3f clacPoint(float u, float v) {
        return new Vector3f( (float) (Math.cos(u)*Math.sin(v)), (float) Math.cos(v), (float) (Math.sin(u)*Math.sin(v)));
    }

}
