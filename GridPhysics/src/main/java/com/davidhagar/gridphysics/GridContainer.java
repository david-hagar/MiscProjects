package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.StateFunction;

public class GridContainer {

    public State[][] grid;

    public GridContainer(StateFunction stateFunction) {
        grid = stateFunction.getInitialGrid();
    }

    public int[] getSize() {
        return new int[]{grid.length, grid[0].length};
    }

    public int getStateSize(){ return grid[0][0].values[0].length;}
}
