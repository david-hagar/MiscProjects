package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.exp.ExpressionFunction;
import com.davidhagar.gridphysics.util.ArrayMath;

import java.util.concurrent.ArrayBlockingQueue;

public class Monitor {
    public static final double MAX_Value = 1e10;
    public static final int ITTERATIONS_TO_STABLE = 50;

    Sim sim;
    private int historySize = 10;
    private ArrayBlockingQueue<GridStats> history = new ArrayBlockingQueue<>(historySize);


    public Monitor(Sim sim) {
        this.sim = sim;
    }


    public void setStatus(String status){
        System.out.println(status);
        Sim.getInstance().status = status;
    }

    public void checkForExit(GridStats gridStats) {
        int minMaxEqualCount = 0;

        int stateSize = sim.stateFunction.getStateSize();
        for (int s = 0; s < stateSize; s++) {
            if (Float.isNaN(gridStats.min[s])) {
                setStatus("gridStats.min[" + s + "] is NaN");
                sim.stop();
            } else if (Float.isNaN(gridStats.max[s])) {
                setStatus("gridStats.max[" + s + "] is NaN");
                sim.stop();
            } else if (Float.isInfinite(gridStats.min[s])) {
                setStatus("gridStats.min[" + s + "] is Infinite");
                sim.stop();
            } else if (Float.isInfinite(gridStats.max[s])) {
                setStatus("gridStats.max[" + s + "] is Infinite");
                sim.stop();
            } else if (Math.abs(gridStats.min[s]) > MAX_Value) {
                setStatus("gridStats.min[" + s + "] >  " + MAX_Value);
                sim.stop();
            } else if (Math.abs(gridStats.max[s]) > MAX_Value) {
                setStatus("gridStats.max[" + s + "] > " + MAX_Value);
                sim.stop();
            } else if (gridStats.min[s] == gridStats.max[s]) {
                minMaxEqualCount++;
            }

        }

        if (minMaxEqualCount == stateSize) {
            setStatus("min == max");
            sim.stop();
        }

        history.add(gridStats);
        if (history.size() == historySize) {
            try {
                GridStats oldStats = history.take();
                if (sim.getLoopCount() > ITTERATIONS_TO_STABLE) {
                    float[] minMaxDiff = ArrayMath.sub(gridStats.max, gridStats.min);
                    float[] minMaxAve = ArrayMath.div(ArrayMath.add(gridStats.max, gridStats.min), 2);
                    float[] minHistoryDiff = ArrayMath.sub(gridStats.min, oldStats.min);
                    float[] maxHistoryDiff = ArrayMath.sub(gridStats.max, oldStats.max);
                }
            } catch (InterruptedException e) {
                // do nothing
                e.printStackTrace();
            }
        }
    }

    private GridStats getHistoryStats() {

        for (GridStats gs : history) {

        }

        return null;
    }


    public void processStop() {
        if(sim.getLoopCount() < 50 ){
            if(sim.stateFunction instanceof ExpressionFunction){
                ExpressionFunction ef = (ExpressionFunction) sim.stateFunction;
                ef.rate(0);
            }

        }

        history.clear();
    }
}
