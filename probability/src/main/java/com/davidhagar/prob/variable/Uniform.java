package com.davidhagar.prob.variable;

public class Uniform implements RandomVariable {

    private final double min;
    private final double max;
    private final double maxMinusMin;

    public Uniform(double min, double max) {
        this.min = min;
        this.max = max;
        this.maxMinusMin = max - min;
    }

    @Override
    public double next() {
        return maxMinusMin * Math.random() + min;
    }


    public ProbabilityDensity getProbabilityDensity(int nBins) {

        ProbabilityDensity pd = new ProbabilityDensity(min, max, nBins);

        double p = (max - min) / nBins;

        for (int i = 0; i < nBins; i++) {
            pd.values[i] = p;
        }

        return pd;
    }


    @Override
    public String toString() {
        return "Uniform(" +
                "min=" + min +
                ", max=" + max +
                ')';
    }
}
