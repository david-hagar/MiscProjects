package com.davidhagar.gridphysics.genetic;

import com.davidhagar.gridphysics.util.RandomUtil;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SimplePopulation implements Population {

    PriorityQueue<GeneticObject> population;
    private final int populationMaxSize;

    public SimplePopulation(int populationMaxSize) {
        this.populationMaxSize = populationMaxSize;
        population = new PriorityQueue<>(populationMaxSize);
    }


    public GeneticObject select() {
        if (population.isEmpty())
            throw new NullPointerException("population is empty.");

        int rIndex = (int) Math.floor(RandomUtil.random.nextFloat() * population.size());

        int i = 0;
        for (GeneticObject go : population) {
            if (i++ == rIndex)
                return go;
        }

        throw new NullPointerException("select did not find."); // should not happen
    }

    @Override
    public GeneticObject add(GeneticObject geneticObject) {
        population.add(geneticObject);

        GeneticObject removed = null;
        if (population.size() > populationMaxSize)
            removed = population.remove();

        return removed;
    }


    public int getSize() {
        return population.size();
    }

    public int getMaxSize() {
        return populationMaxSize;
    }
}
