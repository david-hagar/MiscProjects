package com.davidhagar.prob.variable;

public class NSum implements RandomVariable {


    private final RandomVariable variable;
    private final int numberToAdd;

    public NSum(RandomVariable variable, int numberToAdd) {
        this.variable = variable;
        this.numberToAdd = numberToAdd;
    }

    @Override
    public double next() {
        double sum = 0;
        for (int i = 0; i < numberToAdd; i++) {
            sum += variable.next();
        }

        return sum;
    }

    public ProbabilityDensity getProbabilityDensity(int nBins) {

        ProbabilityDensity varPD = variable.getProbabilityDensity(nBins);

        double min = varPD.min * numberToAdd;
        double max = varPD.max * numberToAdd;

        ProbabilityDensity pd = new ProbabilityDensity(min, max, nBins);

        RecurseSum rs = new RecurseSum();
        RecurseSum sum = recurseAdd(varPD, pd, numberToAdd, rs);

        return pd;
    }

    private RecurseSum recurseAdd( ProbabilityDensity varPD, ProbabilityDensity destPD,  int depth, RecurseSum rs){


        if(depth >0){
            varPD.visit(new ProbabilityDensity.PDFVistor() {
                @Override
                public void visit(double xMin, double xMax, double probability) {
                    RecurseSum thisSum = new RecurseSum(rs);
                    thisSum.min+=xMin;
                    thisSum.max+=xMax;
                    thisSum.probability*=probability;
                }
            });
        }
        else{

        }

        return null;
    }

    private static class RecurseSum {
        double min;
        double max;
        double probability;

        public RecurseSum() {

        }

        public RecurseSum(RecurseSum rs) {
            this.min = rs.min;
            this.max = rs.max;
            this.probability = rs.probability;
        }
    }


    @Override
    public String toString() {
        return "NSum(" +
                "v=" + variable +
                ", n=" + numberToAdd +
                ')';
    }
}
