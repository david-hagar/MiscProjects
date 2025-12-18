package com.davidhagar.gridphysics.genetic.weasel;

import com.davidhagar.gridphysics.genetic.*;
import com.davidhagar.gridphysics.util.RandomUtil;

import java.util.Objects;

import static com.davidhagar.gridphysics.util.RandomUtil.rInt;

public class WeaselGA {


    private static class StringGuess extends GeneticObject {
        private final String value;


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

        @Override
        public String toString() {
            return "StringGuess{" +
                    " fitness='" + getFitness() + '\'' +
                    " value='" + value + '\'' +
                    '}';
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
                int pos = rInt(0, sb.length() - 1);
                sb.setCharAt(pos, (char) (sb.charAt(pos) + rInt(-1, 1)));

                return new StringGuess(sb.toString());
            }
        };
        FitnessFunction fitnessFunction = new FitnessFunction() {
            @Override
            public boolean setFitness(GeneticObject geneticObject) {

                StringGuess sg = (StringGuess) geneticObject;
                String v = sg.value;
                int total = 0;

                int minLength = Math.min(v.length(), goalString.length());
                int maxLength = Math.max(v.length(), goalString.length());
                total+= (maxLength - minLength) * ('~'-' ');
                for (int i = 0; i < minLength ; i++) {
                    total += Math.abs(goalString.charAt(i) - v.charAt(i));
                }

                sg.setFitness(-total);
                return total == 0;
            }
        };
        Population population = new SimplePopulation(20);
        GeneticAlgorithm ga = new GeneticAlgorithm(mutator, fitnessFunction, population);

        for (int i = 0; i < 1000; i++) {
            if( ga.runTillDone(100)){
                System.out.println("best = " + ga.getBest());
                break;
            }
            System.out.println(ga.getIterationCount() + " best = " + ga.getBest());
        }

    }

    private static char getRChar() {
        return (char) rInt(' ', '~');
    }

}
