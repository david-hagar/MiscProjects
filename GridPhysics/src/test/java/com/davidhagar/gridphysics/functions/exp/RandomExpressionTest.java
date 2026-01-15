package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.util.RandomUtil;
import junit.framework.TestCase;
import org.junit.Assert;

public class RandomExpressionTest extends TestCase {

    public void testRInt() {

        int n = 1000;
        int min = 99;
        int max = 0;
        for (int i = 0; i < n; i++) {
            int v = RandomUtil.rInt(-2, 2);
            if (v < min) min = v;
            if (v > max) max = v;

        }

        System.out.println(min);
        System.out.println(max);

        Assert.assertEquals(-2, min);
        Assert.assertEquals(2, max);

    }

    public void testRDouble() {
        int n = 10000000;
        double min = 99;
        double max = 0;
        int maxLimit = 2;
        int minLimit = -2;
        for (int i = 0; i < n; i++) {
            double v = RandomUtil.rFloat(minLimit, maxLimit);
            if (v < min) min = v;
            if (v > max) max = v;

        }

        System.out.println(min);
        System.out.println(max);

        Assert.assertTrue(min > minLimit);
        Assert.assertTrue(max < maxLimit);


        Assert.assertEquals(minLimit, min, 1e-4);
        Assert.assertEquals(maxLimit, max, 1e-4);

    }

    public void testRandomExpression() {

        RandomExpression re = new RandomExpression(2, 3, 999);

        for (int i = 0; i < 20; i++) {
            System.out.println( re.randomExpression() );
        }
    }
}