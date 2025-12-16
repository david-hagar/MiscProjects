package com.davidhagar.gridphysics.functions.exp.op.leaf;

import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonTypeInfo(use = DEDUCTION)
public class GridValue implements Expression {

    int iOffset;
    int jOffset;
    int stateIndex;
    int historyIndex;

    public GridValue() {
    }

    public GridValue(int iOffset, int jOffset, int stateIndex, int historyIndex) {
        this.iOffset = iOffset;
        this.jOffset = jOffset;
        this.stateIndex = stateIndex;
        this.historyIndex = historyIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
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
    public float eval(State[][] grid, int i, int j) {
        return grid[i + iOffset][j + jOffset].values[historyIndex][stateIndex];
    }

    @Override
    public String toString() {
        return "g(" +
                "i+" + iOffset +
                ", j+" + jOffset +
                ')';
    }
}
