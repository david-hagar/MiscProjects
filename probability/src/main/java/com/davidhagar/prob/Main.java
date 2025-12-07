package com.davidhagar.prob;


import com.davidhagar.prob.sample_listener.Histogram;
import com.davidhagar.prob.sample_listener.RVSequenceStorage;
import com.davidhagar.prob.variable.*;

import javax.swing.*;

public class Main {


    public static JFrame frame;

    public static void main(String[] args) {
        //double[][] values = getTestDistribution();
        //double[][] values = getDistribution(new Binomial(100, 0.5), 100000, 200);
        //double[][] values = getDistribution(new Poisson(200), 100000, 400);
        //double[][] values = getDistribution(new Square(new Equal(0,2)), 100000, 400);
        //double[][] values = getDistribution(new Square(new Square(new Binomial(100, 0.5))), 100000, 400);
        double[][] values = getDistribution(new NSum(new Uniform(0,1), 2), 1000000, 400);
        //double[][] values = getDistribution(new NProduct(new Equal(1,2), 8), 1000000, 400);
        //double[][] values = getDistribution(new Equal(10, 20), 100000, 200);

        frame = MainFrame.openFrame(values);
    }

    private static double[][] getTestDistribution() {
        //double values[] = new double[] { 1.0, 2.0, 3.0 };

        int n = 100;
        double[][] values = new double[n][2];
        for (int i = 0; i < values.length; i++) {
            double x = i / (double) values.length * Math.PI / 2;
            values[i][0] = x;
            //System.out.println(x);
            values[i][1] = 100 * Math.cos(x);
        }
        return values;
    }


    public static double[][] getDistribution(RandomVariable randomVariable, int sampleN, int binCount) {

        Sampler s = new Sampler(randomVariable);
        final RVSequenceStorage storage = new RVSequenceStorage(sampleN);
        s.addListener(storage);
        s.sample(sampleN);
        Histogram h = storage.toHistogram(binCount);
        return h.getNormalizedBins();
    }


}




