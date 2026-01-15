package com.davidhagar.gridphysics.util;

import junit.framework.TestCase;
import org.junit.Assert;

public class WeightedRandomSelectorTest extends TestCase {

    public void test() {


        WeightedRandomSelector<Integer> selector = new WeightedRandomSelector<>(999);
        selector.add(0, 1.0);
        selector.add(2, 3.0);
        selector.add(1, 2.0);

        int[] c = new int[3];

        int n = 100*1000*1000;
        for (int i = 0; i < n; i++) {
            int index = selector.selectRandomObject();
            c[index]++;
        }

        for (int i = 0; i < c.length; i++) {
            double prob = c[i] / (double) n;
            double expectedProb = (i + 1) / selector.getTotalWeight();
            System.out.println(i + " = " + c[i] + " ( p = " + prob + " expected = " + expectedProb + " err = " + (prob-expectedProb) + ")");

            Assert.assertEquals(expectedProb, prob, 0.001);
        }
    }
}