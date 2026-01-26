package com.davidhagar.gridphysics.functions;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.State;

import javax.swing.*;

public interface StateFunction {

    public State[][] getInitialGrid();

    public void update(State[][] grid, int i, int j);

    public int getRGB(State state, GridStats gridStats);

    public int getStateSize();
    public int getHistorySize();
    public JPanel getControls();

    public void registerFault();
}
