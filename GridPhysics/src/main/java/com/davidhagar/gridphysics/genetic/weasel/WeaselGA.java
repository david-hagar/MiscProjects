package com.davidhagar.gridphysics.genetic.weasel;

import com.davidhagar.gridphysics.genetic.*;
import com.davidhagar.gridphysics.util.RandomUtil;

import java.util.Objects;

public class WeaselGA {


    private static class StringGuess extends GeneticObject {
        private String value;


        public StringGuess(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            StringGuess that = (StringGuess) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }

    public static void main(String[] args) {

        String goalString = "Me things is looks like a weasel.";


        Mutator mutator = new Mutator() {
            @Override
            public GeneticObject createNew() {
                StringBuilder sb = new StringBuilder(goalString.length());
                for (int i = 0; i < goalString.length(); i++) {
                    sb.append(getRChar());
                }
                return new StringGuess(sb.toString());
            }

            @Override
            public GeneticObject mutate(GeneticObject geneticObject) {
                StringGuess sg = (StringGuess) geneticObject;
                double random = Math.random();
                double lengthChangeProb = 0.1;
                if (random < lengthChangeProb)
                    return new StringGuess(sg.value + getRChar());
                else if (random < lengthChangeProb * 2)
                    return new StringGuess(sg.value.substring(0, sg.value.length() - 1));

                StringBuilder sb = new StringBuilder(sg.value);
                sb.setCharAt(RandomUtil.rInt(0, sb.length() - 1), getRChar());

                return new StringGuess(sb.toString());
            }
        };
        FitnessFunction fitnessFunction = new FitnessFunction() {
            @Override
            public void setFitness(GeneticObject geneticObject) {

                StringGuess sg = (StringGuess) geneticObject;
                String v = sg.value;
                int total = 0;

                for (int i = 0; i < v.length(); i++) {
                    total += Math.abs(goalString.charAt(i) - v.charAt(i));
                }

                sg.setFitness(-total);
            }
        };
        Population population = new SimplePopulation(20, mutator);
        GeneticAlgorithm ga = new GeneticAlgorithm(mutator, fitnessFunction, population);


    }

    private static char getRChar() {
        return (char) RandomUtil.rInt(' ', '~');
    }

}
