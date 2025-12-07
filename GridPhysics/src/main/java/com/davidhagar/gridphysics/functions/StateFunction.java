package com.davidhagar.gridphysics.functions;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.State;

public interface StateFunction {

    State[][] getInitialGrid();

    void update(State[][] grid, int i, int j);

    public int getRGB(State state, GridStats gridStats);
}
