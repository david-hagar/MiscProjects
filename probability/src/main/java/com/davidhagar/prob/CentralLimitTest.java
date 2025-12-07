package com.davidhagar.prob;

import com.davidhagar.prob.sample_listener.RVSequenceStorage;
import com.davidhagar.prob.variable.Uniform;
import com.davidhagar.prob.variable.NSum;
import com.davidhagar.prob.variable.RandomVariable;

public class CentralLimitTest {

    private final RandomVariable randomVariable;
    private int nSamples = 100000;

    public CentralLimitTest(RandomVariable randomVariable) {
        this.randomVariable = randomVariable;
    }

    public void run() {

        printMeanVar();


        for (int i = 1; i <= 10; i++) {
            RVSequenceStorage meanStorage = new RVSequenceStorage(1000);
            RVSequenceStorage varianceStorage = new RVSequenceStorage(1000);

            int sampleSize = i * 10;
            for (int j = 0; j < nSamples; j++) {
                Sampler s = new Sampler(randomVariable);
                final RVSequenceStorage storage = new RVSequenceStorage(1000);
                s.addListener(storage);
                s.sample(sampleSize);
                double m = storage.getMean();
                double var = storage.getVariance(m);
                meanStorage.next(m);
                varianceStorage.next(var);
            }
            double meanMean = meanStorage.getMean();
            double meanVar = meanStorage.getVariance(meanMean);

            double varMean = varianceStorage.getMean();
            double varVar = varianceStorage.getVariance(varMean);

            System.out.println("sampleeSize = " + sampleSize + " mean mean = " + meanMean + " meanVar = " + meanVar +
                    " varMean = " + varMean + " varVar = " + varVar + " varMean/sampleSize = " + varMean/(sampleSize));
        }



    }

    private void printMeanVar() {
        Sampler s = new Sampler(randomVariable);
        final RVSequenceStorage storage = new RVSequenceStorage(1000);
        s.addListener(storage);
        s.sample(1000000);
        double m = storage.getMean();
        double var = storage.getVariance(m);

        System.out.println("mean = " + m + " var = " + var);
    }


    public static void main(String[] args) {
        CentralLimitTest clt = new CentralLimitTest(new NSum(new Uniform(0, 1), 20));
        clt.run();
    }
}
