package com.davidhagar.prob.sample_listener;

public class Histogram implements SampleListener {

    private final int binCount;
    private final double min;
    private final double max;

    private final int values[];
    private int sampleCount = 0;

    public Histogram(int binCount, double min, double max) {
        this.binCount = binCount;
        this.min = min;
        this.max = max;

        values = new int[binCount];
    }

    @Override
    public void next(double value) {
        int binIndex = (int) Math.floor((value - min) / (max - min) * binCount);
        if (binIndex < 0)
            binIndex = 0;
        if (binIndex >= binCount)
            binIndex = binCount - 1;
        values[binIndex]++;
        sampleCount++;
    }

    public double[][] getNormalizedBins() {

        double ret[][] = new double[binCount][2];

        for (int i = 0; i < binCount; i++) {
            ret[i][0] = i / (double) binCount * (max - min) + min;
            ret[i][1] = values[i] /(double) sampleCount;
        }

        return ret;
    }


}
