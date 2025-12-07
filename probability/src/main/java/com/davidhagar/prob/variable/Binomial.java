package com.davidhagar.prob.variable;

public class Binomial implements RandomVariable{

    double p;
    int n;

    public Binomial(int n, double p) {
        this.n = n;
        this.p = p;
    }

    @Override
    public double next() {
        int x = 0;
        for(int i = 0; i < n; i++) {
            if(Math.random() < p)
                x++;
        }
        return x;
    }




    public static int getBinomial(int n, double p) {
        int x = 0;
        for(int i = 0; i < n; i++) {
            if(Math.random() < p)
                x++;
        }
        return x;
    }

    public ProbabilityDensity getProbabilityDensity(int nBins) {
        return null;
    }


    @Override
    public String toString() {
        return "Binomial(" +
                "p=" + p +
                ", n=" + n +
                ')';
    }
}
