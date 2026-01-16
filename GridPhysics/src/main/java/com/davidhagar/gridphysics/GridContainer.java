package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.StateFunction;

public class GridContainer {

    private final StateFunction stateFunction;
    public State[][] grid;

    public GridContainer(StateFunction stateFunction) {
        this.stateFunction = stateFunction;
        reset();
    }


    public int[] getSize() {
        return new int[]{grid.length, grid[0].length};
    }

    public int getStateSize(){ return grid[0][0].values[0].length;}

    public void reset(){
        grid = stateFunction.getInitialGrid();
    }
}
