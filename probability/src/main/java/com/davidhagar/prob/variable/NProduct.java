package com.davidhagar.prob.variable;

public class NProduct implements RandomVariable {


    private final RandomVariable variable;
    private final int numberToAdd;

    public NProduct(RandomVariable variable, int numberToMult) {
        this.variable = variable;
        this.numberToAdd = numberToMult;
    }

    @Override
    public double next() {
        double prod = variable.next();
        final int n = numberToAdd - 1;
        for (int i = 0; i < n; i++) {
            prod *= variable.next();
        }

        return prod;
    }

    @Override
    public ProbabilityDensity getProbabilityDensity(int nBins) {
        return null;
    }


    @Override
    public String toString() {
        return "NProduct{" +
                "v=(" + variable +
                "), n=" + numberToAdd +
                '}';
    }
}
