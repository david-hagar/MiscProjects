package com.davidhagar.serialdata;

public class SmoothingGenerators {

    public static double[][] addMidpoints(double[][] points) {
        double[][] newPoints = new double[points.length * 2][];
        for (int i = 0; i < points.length; i++) {
            newPoints[i * 2] = points[i].clone();
            int next = i < points.length - 1 ? i + 1 : 0;
            newPoints[i * 2 + 1] = average(points[i], points[next]);
        }

        return newPoints;
    }

    public static double[][] smooth(double[][] points, double averageFraction) {
        double[][] newPoints = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            int prev = i == 0 ? points.length - 1 : i - 1;
            int next = i < points.length - 1 ? i + 1 : 0;
            double[] ave = average(points[prev], points[next]);
            mult(ave, 1 - averageFraction);
            double[] v = points[i].clone();
            mult(v, averageFraction);
            newPoints[i] = add(ave, v);
        }
        return newPoints;
    }


    private static double[] average(double[] v1, double[] v2) {
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = (v1[i] + v2[i]) / 2;
        }
        return result;
    }

    private static double[] add(double[] v1, double[] v2) {
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }


    private static void mult(double[] v, double k) {
        for (int i = 0; i < v.length; i++) {
            v[i] *= k;
        }
    }


}
