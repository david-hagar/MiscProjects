package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.functions.exp.RandomExpression;
import com.davidhagar.gridphysics.genetic.*;
import com.davidhagar.gridphysics.genetic.weasel.WeaselGA;

import static com.davidhagar.gridphysics.util.RandomUtil.rInt;

public class ExpressionGA {

    private final Mutator mutator;
    private final FitnessFunction fitnessFunction;
    private final SimplePopulation population;
    private final GeneticAlgorithm ga;
    RandomExpression randomExpression;

    public ExpressionGA(long seed) {
        randomExpression = new RandomExpression(2, 3, seed);
        mutator = new Mutator() {
            @Override
            public GeneticObject createNew() {
                return new ExpressionGO(randomExpression.randomExpression());
            }

            @Override
            public GeneticObject mutate(GeneticObject geneticObject) {

                ExpressionGO ega = (ExpressionGO) geneticObject;

                // todo:

                return null;
            }
        };
         fitnessFunction = new FitnessFunction() {
            @Override
            public boolean setFitness(GeneticObject geneticObject) {

                throw new NullPointerException("not implemented");
                //return 0;
            }
        };
         population = new SimplePopulation(20);
         ga = new GeneticAlgorithm(mutator, fitnessFunction, population);

    }
}
