package com.davidhagar.gridphysics;

public class GridStats {

    public float [] min;
    public float [] max;
    public double average = 0;


    public GridStats(float [] min, float []max, double average) {
        this.min = min;
        this.max = max;
        this.average = average;
    }
}
