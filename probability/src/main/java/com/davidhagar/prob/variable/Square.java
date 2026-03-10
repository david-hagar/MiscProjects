package com.davidhagar.prob.variable;

public class Square implements RandomVariable {


    private final RandomVariable variable;

    public Square(RandomVariable variable) {

        this.variable = variable;
    }

    @Override
    public double next() {
        final double next = variable.next();
        return next * next;
    }

    @Override
    public ProbabilityDensity getProbabilityDensity(int nBins) {
        return null;
    }


    @Override
    public String toString() {
        return "Square{" +
                "v=(" + variable +
                ")}";
    }
}
