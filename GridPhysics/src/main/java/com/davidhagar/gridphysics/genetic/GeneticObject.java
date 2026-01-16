package com.davidhagar.gridphysics.genetic;

public abstract class GeneticObject implements Comparable<GeneticObject> {

    double fitness = Double.NaN;

    public GeneticObject() {
    }

    public GeneticObject(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }


//    @Override
//    public abstract boolean equals(Object o);
//
//    @Override
//    public abstract int hashCode();


    @Override
    public int compareTo(GeneticObject o) {
        return Double.compare(fitness, o.fitness);
    }
}
