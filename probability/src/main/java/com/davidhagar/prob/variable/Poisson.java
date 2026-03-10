package com.davidhagar.prob.variable;

public class Poisson implements RandomVariable {


    private final double lambda;

    public Poisson(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public double next() {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public ProbabilityDensity getProbabilityDensity(int nBins) {
        return null;
    }

    @Override
    public String toString() {
        return "Poisson{" +
                "lambda=" + lambda +
                '}';
    }
}
