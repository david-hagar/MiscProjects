package com.davidhagar.gridphysics.functions;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.util.Util;

public class SimpleFunction implements StateFunction {
    @Override
    public State[][] getInitialGrid() {

        int width = 600;
        int height = 400;
        State[][] values = new State[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                values[i][j] = new State(new float[]{(i / (float) width * 255) % 255,
                        (j / (float) height * 255) % 255,
                        (i + j) % 255}, new float[]{0, 0, 0}, new float[]{0, 0, 0});
            }
        }

        return values;
    }

    @Override
    public void update(State[][] grid, int i, int j) {
        //System.out.println(i +" " + j);
        State state = grid[i][j];
        int iPlusOne = (i + 1) % grid.length;
        System.arraycopy(grid[iPlusOne][j].values[1], 0, state.values[0], 0, state.values[0].length);
    }

    @Override
    public int getRGB(State state, GridStats gridStats) {
        float[] v = state.values[0];
        return Util.convertRGBToInt((int) v[0], (int) v[1], (int) v[2]);
    }
}
