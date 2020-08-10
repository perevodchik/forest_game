package com.perevodchik.forest.utils;

import com.badlogic.gdx.math.Vector2;

public class Vector {

    public static double distance(Vector2 v0, Vector2 v1){
        return Math.sqrt(Math.pow((v1.x - v0.x), 2) + Math.pow((v1.y - v0.y), 2));
    }

}
