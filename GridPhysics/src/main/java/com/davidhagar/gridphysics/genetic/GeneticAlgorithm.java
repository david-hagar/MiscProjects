package com.davidhagar.gridphysics.genetic;

import java.util.ArrayList;

public class GeneticAlgorithm {


    private final Mutator mutator;
    private final FitnessFunction fitnessFunction;
    private final Population population;

    public GeneticAlgorithm(Mutator mutator, FitnessFunction fitnessFunction, Population population) {
        this.mutator = mutator;
        this.fitnessFunction = fitnessFunction;
        this.population = population;
    }
}
