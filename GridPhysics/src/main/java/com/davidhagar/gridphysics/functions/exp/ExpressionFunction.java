package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.StateFunction;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.davidhagar.gridphysics.util.Util;

import javax.swing.*;
import java.awt.*;


public class ExpressionFunction implements StateFunction {

    private static final boolean color = true;

    final int stateSize;
    final int historySize;
    Expression[] stateFunction;
    float[] tmpScaled ;

    public ExpressionFunction(int stateSize, int historySize, long seed) {
        this.stateSize = stateSize;
        this.historySize = historySize;

        tmpScaled = new float[stateSize];
        stateFunction = new Expression[stateSize];

        RandomExpression re = new RandomExpression(stateSize, historySize, seed);
        for (int s = 0; s < this.stateSize; s++)
            stateFunction[s] = re.randomExpression();
    }

    @Override
    public State[][] getInitialGrid() {

        int width = 512;
        int height = 512;
        State[][] values = new State[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                values[i][j] = new State(stateSize, historySize);
            }
        }

        initRandom(width, height, values, 100, 5000);

        return values;
    }

    private void initRandom(int width, int height, State[][] values, int spread, int n) {
        int wo2 = width / 2;
        int h02 = height / 2;

        for (int i = 0; i < n; i++) {
            int iPos = (int) (wo2 + (Math.random() * 2 - 1) * spread);
            int jPos = (int) (h02 + (Math.random() * 2 - 1) * spread);
            for (int s = 0; s < stateSize; s++)
                values[iPos][jPos].values[0][s] = RandomUtil.rFloat(0, 1);
        }
    }


    @Override
    public void update(State[][] grid, int i, int j) {
        for (int s = 0; s < stateSize; s++) {
            grid[i][j].values[0][s] = stateFunction[s].eval(grid, i, j);
        }
    }


    @Override
    public int getRGB(State state, GridStats gridStats) {
        if (stateSize == 1) {
            float[] v = state.values[0];
            float iFloat = scaleMinMaxS(gridStats, v[0]);
            if (color) {
                return Color.HSBtoRGB(iFloat, 1, 1);
            } else {
                int i = (int) (iFloat * 255);
                return Util.convertRGBToInt(i, i, i);
            }
        } else if (stateSize == 2) {
            scaleMinMax(gridStats, state.values[0], tmpScaled);
            return Color.HSBtoRGB(tmpScaled[0], 1, tmpScaled[1]);
        }
        else if (stateSize == historySize) {
            scaleMinMax(gridStats, state.values[0], tmpScaled);
            int r = (int)(tmpScaled[0] * 255);
            int g = (int)(tmpScaled[1] * 255);
            int b = (int)(tmpScaled[2] * 255);
            return 0xff000000 | (r << 16) | (g << 8) | b;
        }
        else throw new RuntimeException("not implemented stateSize = " + stateSize);
    }

    private static float scaleMinMaxS(GridStats gridStats, float v) {
        return (v - gridStats.min[0]) / (gridStats.max[0] - gridStats.min[0]);
    }

    private void scaleMinMax(GridStats gridStats, float[] src, float[] dest) {

        for (int s = 0; s < stateSize; s++) {
            dest[s] = (src[s] - gridStats.min[s]) / (gridStats.max[s] - gridStats.min[s]);
        }
    }

    public int getStateSize() {
        return stateSize;
    }

    public int getHistorySize(){
        return historySize;
    }


    @Override
    public JPanel getControls() {
        return new JPanel();
    }

    public void rate(float score) {

    }

    public void save() {

    }
}
