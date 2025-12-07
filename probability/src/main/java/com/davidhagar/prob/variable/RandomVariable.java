package com.davidhagar.prob.variable;

public interface RandomVariable {

    public double next();


    public ProbabilityDensity getProbabilityDensity(int nBins);
}
