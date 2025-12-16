package com.davidhagar.gridphysics.genetic;

public interface Mutator {


    GeneticObject createNew();
    GeneticObject mutate(GeneticObject geneticObject);

}
