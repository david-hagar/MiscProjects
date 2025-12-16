package com.davidhagar.gridphysics.util;

public class RandomUtil {
    public static int rInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static float rDouble(float min, float max) {
        return (float) (min + Math.random() * (max - min));
    }
}
