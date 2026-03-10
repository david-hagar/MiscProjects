package com.davidhagar.prob.variable;

public interface RandomVariable {

    double next();


    ProbabilityDensity getProbabilityDensity(int nBins);
}
