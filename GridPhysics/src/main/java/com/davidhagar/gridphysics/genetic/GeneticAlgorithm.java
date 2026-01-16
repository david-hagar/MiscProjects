package com.davidhagar.gridphysics.genetic;

public class GeneticAlgorithm {


    private final Mutator mutator;
    private final FitnessFunction fitnessFunction;
    private final Population population;
    private GeneticObject best = null;
    private int iterationCount = 0;

    public GeneticAlgorithm(Mutator mutator, FitnessFunction fitnessFunction, Population population) {
        this.mutator = mutator;
        this.fitnessFunction = fitnessFunction;
        this.population = population;

        initialize();
    }

    private void initialize() {

        int maxSize = population.getMaxSize();
        for (int i = 0; i < maxSize; i++) {
            GeneticObject geneticObject = mutator.createNew();
            fitnessFunction.setFitness(geneticObject);
            population.add(geneticObject);
        }
    }


    public boolean oneStep() {
        GeneticObject so = population.select();
        GeneticObject mo = mutator.mutate(so);
        boolean done = fitnessFunction.setFitness(mo);
        population.add(mo);

        if (best == null || mo.compareTo(best) > 0)
            best = mo;

        iterationCount++;
        return done;
    }

    public boolean runTillDone(int maxSteps) {

        for (int i = 0; i < maxSteps; i++) {
            if (oneStep())
                return true;
        }

        return false;
    }

    public GeneticObject getBest() {
        return best;
    }

    public int getIterationCount() {
        return iterationCount;
    }


    public GeneticObject getNextManual() {
        GeneticObject so = population.select();
        GeneticObject mo = mutator.mutate(so);
        return mo;
    }

    public void addRatedManual(GeneticObject ro){
        if(Double.isNaN( ro.getFitness() ))
            throw new NullPointerException("no fitness");
        population.add(ro);
    }
}
