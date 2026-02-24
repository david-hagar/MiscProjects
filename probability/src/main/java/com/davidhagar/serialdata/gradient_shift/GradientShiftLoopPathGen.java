package com.davidhagar.serialdata.gradient_shift;

public class GradientShiftLoopPathGen {


    private final int nDim;
    private final float phaseOffsetFraction;
    private final int n;
    private final boolean useSin;

    public GradientShiftLoopPathGen(int nDim, float phaseOffsetFraction, int n, boolean useSin) {
        this.nDim = nDim;
        this.phaseOffsetFraction = phaseOffsetFraction;
        this.n = n;
        this.useSin = useSin;
    }

    public double[][] generate() {

        double[][] values = new double[n][nDim];
        int phaseOffset = (int) (n * phaseOffsetFraction);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < nDim; j++) {
                double i1 = (i  + phaseOffset * j) % n;
                if (useSin)
                    values[i][j] = getSinValue(i1, n);
                else {
                    values[i][j] = getGradValue(i1, n);
                }
            }
        }

        return values;
    }


    static double getGradValue(double i, int n) {
        double half = n / 2.0;
        double v = i / (double) n;
        if (i < half)
            return v;
        else
            return 1 - v;
    }

    static double getSinValue(double i, int n) {
        return Math.sin(i / (double) n * Math.PI * 2);
    }

}
