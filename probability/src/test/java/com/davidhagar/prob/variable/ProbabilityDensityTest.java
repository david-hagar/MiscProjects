package com.davidhagar.prob.variable;

import junit.framework.TestCase;

public class ProbabilityDensityTest extends TestCase {

    public void testXToBinIndex() {

        ProbabilityDensity pd = new ProbabilityDensity(1, 4, 3);

        double epsilon = 1e-5;

        assertEquals(0, pd.xToBinIndexInt(1));
        assertEquals(0, pd.xToBinIndexInt(2 - epsilon));
        assertEquals(1, pd.xToBinIndexInt(2));
        assertEquals(1, pd.xToBinIndexInt(2 + epsilon));
        assertEquals(1, pd.xToBinIndexInt(3 - epsilon));
        assertEquals(2, pd.xToBinIndexInt(3));
        assertEquals(2, pd.xToBinIndexInt(4 - epsilon));
        //assertEquals(2, pd.xToBinIndexInt(4)) ;

        assertEquals(1.0, pd.binIndexToXMin(0), 1e-5);
        assertEquals(2.0, pd.binIndexToXMin(1), 1e-5);
        assertEquals(3.0, pd.binIndexToXMin(2), 1e-5);

        double[][] values = {
                {1.0, 2.0, 0},
                {2.0, 3.0, 0},
                {3.0, 4.0, 0}
        };
        pd.visit(new ProbabilityDensity.PDFVistor() {
            int i=0;
            @Override
            public void visit(double xMin, double xMax, double probability) {
                System.out.println(xMin + " " + xMax + " " + probability);
                assertEquals(values[i][0], xMin);
                assertEquals(values[i][1], xMax);
                assertEquals(values[i][2], probability);
                i++;
            }
        });
    }
}