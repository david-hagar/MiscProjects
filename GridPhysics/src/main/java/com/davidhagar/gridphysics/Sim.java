package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.SimpleFunction;
import com.davidhagar.gridphysics.functions.StateFunction;
import com.davidhagar.gridphysics.functions.WaveFunction;
import com.davidhagar.gridphysics.functions.exp.ExpressionFunction;

public class Sim {

    public static final int loopDelay = 500;
    private static Sim globalInstance = null;

    private final Monitor monitor;
    public GridContainer gridContainer;
    public StateFunction stateFunction;
    private Thread thread;
    private Runnable loopListener;
    private int loopCount = 0;
    private int paintDelay = 4;
    public long randomSeed = 123456789;

    public Sim(StateFunction stateFunction) {

        this.gridContainer = new GridContainer(stateFunction);
        this.stateFunction = stateFunction;
        this.monitor = new Monitor(this);

        if(globalInstance != null)
            throw new NullPointerException("One instance allowed.");

        globalInstance = this;
    }

    public static Sim makeSimple() {
        SimpleFunction f = new SimpleFunction();
        return new Sim(f);
    }

    public static Sim makeWave() {
        WaveFunction f = new WaveFunction();
        return new Sim(f);
    }

    public static Sim makeExpression() {
        ExpressionFunction f = new ExpressionFunction(1, 3, 999);
        return new Sim(f);
    }

    public static Sim getInstance() {
        return globalInstance;
    }

    public void runOneStep() {
        synchronized (this) {
            State[][] gridArray = gridContainer.grid;

            for (State[] states : gridArray)
                for (State state : states)
                    state.swapPrevAndZero();

            int length1 = gridArray.length;
            final int length2 = gridArray[0].length;
            for (int i = 0; i < length1; i++) {
                for (int j = 0; j < length2; j++) {
                    stateFunction.update(gridArray, i, j);
                }
            }

            loopCount++;
        }
    }

    public void start() {
        if (thread != null)
            return;

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < paintDelay; i++) {
                    runOneStep();
                }

                if (loopDelay > 0)
                    try {
                        Thread.sleep(loopDelay);
                    } catch (InterruptedException e) {
                        break;
                    }
                loopListener.run();
            }
            thread = null;
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }


    public void stop() {
        if (thread != null)
            thread.interrupt();
    }


    public void setLoopListener(Runnable loopListener) {
        this.loopListener = loopListener;
    }


    public int getLoopCount() {
        return loopCount;
    }


    public GridStats getStats() {

        float[] firstStateValue = gridContainer.grid[0][0].values[0];
        int stateSize = firstStateValue.length;

        float[] min = new float[stateSize];
        float[] max = new float[stateSize];

        for (int s = 0; s < stateSize; s++) {
            min[s] = firstStateValue[s];
            max[s] = firstStateValue[s];
        }

        for (int s = 0; s < stateSize; s++) {
            for (State[] states : gridContainer.grid)
                for (State state : states) {
                    float v = state.values[0][s];
                    if (min[s] > v)
                        min[s] = v;
                    if (max[s] < v)
                        max[s] = v;
                }
        }
        GridStats gridStats = new GridStats(min, max);
        monitor.checkForExit(gridStats);
        return gridStats;
    }


}
