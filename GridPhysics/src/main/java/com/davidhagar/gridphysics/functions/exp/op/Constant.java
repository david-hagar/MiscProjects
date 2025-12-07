package com.davidhagar.gridphysics.functions.exp.op;

import com.davidhagar.gridphysics.State;

import java.util.Arrays;

public class Constant implements Expression{

    float[] value;


    public Constant() {
    }

    public Constant(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    @Override
    public float[] eval(State[][] grid, int i, int j) {
        return new float[0];
    }


    @Override
    public String toString() {
        return Arrays.toString(value);
    }
}
