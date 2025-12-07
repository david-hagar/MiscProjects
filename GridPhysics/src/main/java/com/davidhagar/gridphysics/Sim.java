package com.davidhagar.gridphysics;

import com.davidhagar.gridphysics.functions.SimpleFunction;
import com.davidhagar.gridphysics.functions.StateFunction;
import com.davidhagar.gridphysics.functions.WaveFunction;

public class Sim {

    public static final int loopDelay = 1;
    public Grid grid;
    public StateFunction stateFunction;
    private Thread thread;
    private Runnable loopListener;
    private int loopCount = 0;
    private int paintDelay = 4;

    public Sim(StateFunction stateFunction) {

        this.grid = new Grid(stateFunction);
        this.stateFunction = stateFunction;
    }

    public static Sim makeSimple() {
        SimpleFunction f = new SimpleFunction();
        return new Sim(f);
    }

    public static Sim makeWave() {
        WaveFunction f = new WaveFunction();
        return new Sim(f);
    }

    public void runOneStep() {
        synchronized (this) {
            State[][] gridArray = grid.grid;


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
        float min = grid.grid[0][0].values[0][0];
        float max = min;
        for (State[] states : grid.grid)
            for (State state : states) {
                float v = state.values[0][0];
                if (min > v)
                    min = v;
                if (max < v)
                    max = v;
            }

        return new GridStats(min, max);
    }
}
