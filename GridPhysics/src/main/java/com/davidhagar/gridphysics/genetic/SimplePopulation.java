package com.davidhagar.gridphysics.genetic;

import java.util.PriorityQueue;

public class SimplePopulation implements Population {

    PriorityQueue<GeneticObject> population;

    public SimplePopulation(int populationSize, Mutator initializer) {
        population = new PriorityQueue<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            population.add(initializer.createNew());
        }
    }


    public GeneticObject select() {
        int rIndex = (int) Math.floor(Math.random() * population.size());

        int i = 0;
        for (GeneticObject go : population) {
            if (i++ == rIndex)
                return go;
        }

        return null; // should not happen
    }

    @Override
    public void add(GeneticObject geneticObject) {
        population.add(geneticObject);
        population.remove();
    }


}
