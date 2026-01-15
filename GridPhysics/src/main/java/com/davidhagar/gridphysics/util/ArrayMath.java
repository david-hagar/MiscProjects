package com.davidhagar.gridphysics.util;

public class ArrayMath {


    public static float[] add(float[] arr1, float[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must be of the same length.");
        }

        int length = arr1.length;
        float[] result = new float[length];

        for (int i = 0; i < length; i++) {
            result[i] = arr1[i] + arr2[i];
        }

        return result;
    }


    public static float[] sub(float[] arr1, float[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must be of the same length.");
        }

        int length = arr1.length;
        float[] result = new float[length];

        for (int i = 0; i < length; i++) {
            result[i] = arr1[i] - arr2[i];
        }

        return result;
    }

    public static float[] abs(float[] arr1) {


        int length = arr1.length;
        float[] result = new float[length];

        for (int i = 0; i < length; i++) {
            result[i] = Math.abs(arr1[i]);
        }

        return result;
    }


    public static float[] mult(float[] arr1, float scalarValue) {
        int length = arr1.length;
        float[] result = new float[length];

        for (int i = 0; i < length; i++) {
            result[i] = arr1[i] * scalarValue;
        }

        return result;
    }

    public static float[] div(float[] arr1, float scalarValue) {
        int length = arr1.length;
        float[] result = new float[length];

        for (int i = 0; i < length; i++) {
            result[i] = arr1[i] / scalarValue;
        }

        return result;
    }
}
