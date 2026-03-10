package com.davidhagar.serialdata.gradient_shift;

import junit.framework.TestCase;

public class GradientShiftLoopPathGenTest extends TestCase {

    public void testGradient() {
        int n = 10;
        for (int i = 0; i < n; i++) {
            double v = GradientShiftLoopPathGen.getGradValue(i, n);
            System.out.println(i + "\t" + v + "\t" + GradientShiftLoopPathGen.getGradValue(i, n));
        }
    }

    public void testGenerate() {
        GradientShiftLoopPathGen g = new GradientShiftLoopPathGen(5, 0.25f, 10, true);
        double[][] v = g.generate();

        for (double[] vE : v) {
            for (double d : vE) {
                System.out.printf("%f", d);
                System.out.print(", ");
            }
            System.out.println();
        }

    }
}