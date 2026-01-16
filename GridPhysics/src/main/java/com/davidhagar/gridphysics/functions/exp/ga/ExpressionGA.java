package com.davidhagar.gridphysics.functions.exp.ga;

import com.davidhagar.gridphysics.functions.StateFunction;
import com.davidhagar.gridphysics.functions.exp.RandomExpression;
import com.davidhagar.gridphysics.functions.exp.op.Expression;
import com.davidhagar.gridphysics.genetic.*;

import java.io.File;

public class ExpressionGA {

    private final Mutator mutator;
    private final FitnessFunction fitnessFunction;
    private final SimplePopulation population;
    private final GeneticAlgorithm ga;
    RandomExpression randomExpression;

    public ExpressionGA(StateFunction stateFunction, long seed) {
        randomExpression = new RandomExpression(stateFunction.getStateSize(), stateFunction.getHistorySize(), seed);
        mutator = new Mutator() {

            ExpressionMutator em = new ExpressionMutator(stateFunction, seed);

            @Override
            public GeneticObject createNew() {
                return new ExpressionGO(randomExpression.getNExpressions(stateFunction.getStateSize()));
            }

            @Override
            public GeneticObject mutate(GeneticObject geneticObject) {
                ExpressionGO expressionGO = (ExpressionGO) geneticObject;

                Expression[] expressions = new Expression[expressionGO.expressions.length];
                for (int i = 0; i < expressionGO.expressions.length; i++) {
                    expressions[i] = em.mutate(expressionGO.expressions[i]);
                }

                return new ExpressionGO(expressions);
            }
        };
        fitnessFunction = new FitnessFunction() {
            @Override
            public boolean setFitness(GeneticObject geneticObject) {
               geneticObject.setFitness(0);
               return false;
            }
        };
        population = new SimplePopulation(20);
        ga = new GeneticAlgorithm(mutator, fitnessFunction, population);
    }

    public Expression[] getNext() {
        ExpressionGO go = (ExpressionGO) ga.getNextManual();
        return go.expressions;
    }

    public void storeExpression(Expression[] e, double fitness) {
        ExpressionGO ego = new ExpressionGO(e);
        ego.setFitness(fitness);
        ga.addRatedManual(ego);
    }

    public void save(File file){

    }


    public void load(File file){

    }



}
