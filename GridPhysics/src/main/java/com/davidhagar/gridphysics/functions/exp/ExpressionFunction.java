package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.functions.StateFunction;
import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGA;
import com.davidhagar.gridphysics.functions.exp.io.ExpressionContainer;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.functions.exp.op.bin.Mult;
import com.davidhagar.gridphysics.functions.exp.op.bin.Sub;
import com.davidhagar.gridphysics.functions.exp.op.leaf.Constant;
import com.davidhagar.gridphysics.functions.exp.op.leaf.GridValue;
import com.davidhagar.gridphysics.util.RandomUtil;
import com.davidhagar.gridphysics.util.Util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class ExpressionFunction implements StateFunction {

    private static final boolean color = true;

    final int stateSize;
    final int historySize;
    Expression[] stateFunction;
    float[] tmpScaled;

    ExpressionGA expressionGA;
    private RatePanel ratePanel;

    public ExpressionFunction(int stateSize, int historySize, long seed) {
        this.stateSize = stateSize;
        this.historySize = historySize;

        tmpScaled = new float[stateSize];

        RandomExpression re = new RandomExpression(stateSize, historySize, seed);

        expressionGA = new ExpressionGA(this, seed);
        stateFunction = expressionGA.getNext();
        ratePanel = new RatePanel(this);
        updateExpressionDisplay();

        load();
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
        } else if (stateSize == historySize) {
            scaleMinMax(gridStats, state.values[0], tmpScaled);
            int r = (int) (tmpScaled[0] * 255);
            int g = (int) (tmpScaled[1] * 255);
            int b = (int) (tmpScaled[2] * 255);
            return 0xff000000 | (r << 16) | (g << 8) | b;
        } else throw new RuntimeException("not implemented stateSize = " + stateSize);
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

    public int getHistorySize() {
        return historySize;
    }


    @Override
    public JPanel getControls() {
        return ratePanel;
    }

    public void rate(float score) {
        Sim.getInstance().runInMainLoop(new Runnable() {
            @Override
            public void run() {
                expressionGA.storeExpression(stateFunction, score);

                stateFunction = expressionGA.getNext();
                updateExpressionDisplay();
                Sim.getInstance().reset();
            }
        });
    }

    private void updateExpressionDisplay() {
        StringBuilder sb = new StringBuilder(500);
        for (int i = 0; i < stateFunction.length; i++) {
            sb.append(stateFunction[i].toString());
            if (i != stateFunction.length - 1)
                sb.append("\n");
        }
        ratePanel.setExpression(sb.toString());
    }

    public void save() throws IOException {
        expressionGA.getPopulation().toExpressionContainer().save(getFile());
        System.out.println("saved.");
    }

    private File getFile() {
        return new File("./save.json");
    }

    public void load() {
        File file = getFile();
        if (file.exists()) {
            try {
                expressionGA.getPopulation().setPopulation(ExpressionContainer.load(file));
                System.out.println("Loaded.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Expression getWaveFunction() {

        //        float[][] values = grid[i][j].values;
        //        values[0][0] = 2 * values[1][0] - values[2][0] +
        //                CSquared * (grid[iP1][j].values[1][0] + grid[iM1][j].values[1][0] +
        //                        grid[i][jP1].values[1][0] + grid[i][jM1].values[1][0] -
        //                        4 * values[1][0]);

        Expression e1 = new Sub(
                new Mult(
                        new Constant(2), new GridValue(0, 0, 2, 0)),
                new GridValue(0, 0, 2, 0)
        );


        return null;
    }


    private static float getCSquared() {
        float c = 1.0f; // Wave speed
        float dt = 0.5f; // Time step
        float dx = 1.0f; // Spatial step (assuming dx = dy)
        float C = (c * dt / dx);
        float CSquared = C * C;

        System.out.println("CSquared = " + CSquared);
        System.out.println("1/sqrt(2) = " + 1 / Math.sqrt(2));
        return CSquared;
    }

    @Override
    public void registerFault() {
        rate(0);
    }
}
