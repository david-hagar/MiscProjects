package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.StateFunction;

public class Grid {

    public State [][] grid;

    public Grid(StateFunction stateFunction) {
        grid = stateFunction.getInitialGrid();
    }



    public int[] getSize() {
        return new int[]{grid.length, grid[0].length};
    }
}
