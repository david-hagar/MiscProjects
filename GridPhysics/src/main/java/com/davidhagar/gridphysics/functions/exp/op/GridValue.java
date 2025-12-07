package com.davidhagar.gridphysics.functions.exp.op;

import com.davidhagar.gridphysics.State;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonTypeInfo(use = DEDUCTION)
public class GridValue implements Expression{

    int iOffset;
    int jOffset;

    public GridValue() {
    }

    public GridValue(int iOffset, int jOffset) {
        this.iOffset = iOffset;
        this.jOffset = jOffset;
    }

    public int getiOffset() {
        return iOffset;
    }

    public void setiOffset(int iOffset) {
        this.iOffset = iOffset;
    }

    public int getjOffset() {
        return jOffset;
    }

    public void setjOffset(int jOffset) {
        this.jOffset = jOffset;
    }

    @Override
    public float[] eval(State[][] grid, int i, int j) {
        return new float[0];
    }

    @Override
    public String toString() {
        return "g(" +
                "i+" + iOffset +
                ", j+" + jOffset +
                ')';
    }
}
