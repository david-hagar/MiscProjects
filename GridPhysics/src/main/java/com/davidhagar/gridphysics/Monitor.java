package com.davidhagar.gridphysics;

public class Monitor {
    public static final double MAX_Value = 1e10;
    public static final int ITTERATIONS_TO_STABLE = 50;

    Sim sim;
    private int historySize = 10;
//    private ArrayBlockingQueue<GridStats> history = new ArrayBlockingQueue<>(historySize);


    public Monitor(Sim sim) {
        this.sim = sim;
    }


    public void registerFault(String status) {
        Sim sim = Sim.getInstance();
        sim.status = status;

        sim.runInMainLoop(new Runnable() {
            @Override
            public void run() {
                sim.stateFunction.registerFault();
            }
        });
    }

    public void checkForExit(GridStats gridStats) {
        int minMaxEqualCount = 0;

        int stateSize = sim.stateFunction.getStateSize();
        for (int s = 0; s < stateSize; s++) {
            if (Float.isNaN(gridStats.min[s])) {
                registerFault("gridStats.min[" + s + "] is NaN");
            } else if (Float.isNaN(gridStats.max[s])) {
                registerFault("gridStats.max[" + s + "] is NaN");
            } else if (Float.isInfinite(gridStats.min[s])) {
                registerFault("gridStats.min[" + s + "] is Infinite");
            } else if (Float.isInfinite(gridStats.max[s])) {
                registerFault("gridStats.max[" + s + "] is Infinite");
            } else if (Math.abs(gridStats.min[s]) > MAX_Value) {
                registerFault("gridStats.min[" + s + "] >  " + MAX_Value);
            } else if (Math.abs(gridStats.max[s]) > MAX_Value) {
                registerFault("gridStats.max[" + s + "] > " + MAX_Value);
            } else if (gridStats.min[s] == gridStats.max[s]) {
                minMaxEqualCount++;
            }

        }

        if (minMaxEqualCount == stateSize) {
            registerFault("min == max");
        }

//        history.add(gridStats);
//        if (history.size() == historySize) {
//            try {
//                GridStats oldStats = history.take();
//                if (sim.getLoopCount() > ITTERATIONS_TO_STABLE) {
//                    float[] minMaxDiff = ArrayMath.sub(gridStats.max, gridStats.min);
//                    float[] minMaxAve = ArrayMath.div(ArrayMath.add(gridStats.max, gridStats.min), 2);
//                    float[] minHistoryDiff = ArrayMath.sub(gridStats.min, oldStats.min);
//                    float[] maxHistoryDiff = ArrayMath.sub(gridStats.max, oldStats.max);
//                }
//            } catch (InterruptedException e) {
//                // do nothing
//                e.printStackTrace();
//            }
//        }
    }

//    private GridStats getHistoryStats() {
//
//        for (GridStats gs : history) {
//
//        }
//
//        return null;
//    }


}
