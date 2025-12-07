package com.davidhagar.prob.variable;

public class ProbabilityDensity {

    double min;
    double max;
    double[] values;


    public ProbabilityDensity(double min, double max, int bins) {
        this.min = min;
        this.max = max;
        values = new double[bins];
    }

    public double[] getValues() {
        return values;
    }

    public void normalize() {
        double total = 0;
        for (double value : values) total += value;

        for (int i = 0; i < values.length; i++)
            values[i] /= total;
    }

    public double xToBinIndex(double x) {
        return (x - min) / (max - min) * (values.length);
    }

    public int xToBinIndexInt(double x) {
        return (int) Math.floor(xToBinIndex(x));
    }


    public double binIndexToXMin(int i) {
        return (i / (double) (values.length)) * (max - min) + min;
    }


    public double getProbability(double minX, double maxX) {

        return -1;
    }

    public void visit(PDFVistor vistor) {

        double binWidth = getBinWidth();
        for (int i = 0; i < values.length; i++) {
            double x = binIndexToXMin(i);
            vistor.visit(x, x + binWidth, values[i]);
        }
    }

    private double getBinWidth() {
        return (max - min) / values.length;
    }


    public interface PDFVistor {
        public void visit(double xMin, double xMax, double probability);
    }
}
