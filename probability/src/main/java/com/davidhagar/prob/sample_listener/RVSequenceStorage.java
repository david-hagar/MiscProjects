package com.davidhagar.prob.sample_listener;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

public class RVSequenceStorage implements SampleListener {

    DoubleArrayList store;

    public RVSequenceStorage(int capacity) {

        store = new DoubleArrayList(capacity);
    }

    @Override
    public void next(double value) {
        store.add(value);
    }


    public Histogram toHistogram(int binCount) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double d : store) {
            if (d < min)
                min = d;
            if (d > max)
                max = d;
        }

        Histogram h = new Histogram(binCount, min, max);

        for (double d : store) {
            h.next(d);
        }

        return h;
    }

    public double getMean() {
        double total = 0;
        for (double d : store) {
            total += d;
        }
        return total / store.size();
    }

    public double getVariance(double mean) {
        double total = 0;
        for (double d : store) {
            double x = mean - d;
            total += x * x;
        }
        return total / store.size();
    }
}
