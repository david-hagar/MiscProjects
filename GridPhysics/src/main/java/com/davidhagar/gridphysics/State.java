package com.davidhagar.gridphysics;

import java.util.Arrays;

public class State {

    public float[][] values;


    public State(int size, int historySize) {
        values = new float[historySize][size];
    }

    public State(float[] value0,float[] value1,float[] value2) {
        values = new float[3][];
        values[0] = value0;
        values[1] = value1;
        values[2] = value2;
    }


    public void swapPrevAndZero() {
        float[] tmp = values[values.length - 1];

        for (int i = values.length - 1; i > 0; i--)
            values[i] = values[i - 1];

        values[0] = tmp;
        Arrays.fill(tmp, 0);
    }
}
