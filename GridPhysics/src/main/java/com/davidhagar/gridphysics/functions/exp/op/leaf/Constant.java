package com.davidhagar.gridphysics.functions.exp.op.leaf;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;

public class Constant implements Expression {

    float value;


    public Constant() {
    }

    public Constant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public float eval(State[][] grid, int i, int j) {
        return value;
    }


    @Override
    public String toString() {
        return Float.toString(value);
    }
}
