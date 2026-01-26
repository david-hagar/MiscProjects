package com.davidhagar.gridphysics.genetic;

import com.davidhagar.gridphysics.functions.exp.ga.ExpressionGO;
import com.davidhagar.gridphysics.functions.exp.io.ExpressionContainer;
import com.davidhagar.gridphysics.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collection;
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

    public ExpressionContainer toExpressionContainer(){
        ArrayList<ExpressionGO> list = new ArrayList<ExpressionGO>();
        for(GeneticObject o :population)
            list.add((ExpressionGO) o);


        return new ExpressionContainer(list);
    }

    public void setPopulation(ExpressionContainer ec){
        population.clear();
        population.addAll(ec.getExpressions());
    }


    public int getSize() {
        return population.size();
    }

    public int getMaxSize() {
        return populationMaxSize;
    }
}
