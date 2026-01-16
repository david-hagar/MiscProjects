package com.davidhagar.gridphysics.functions.exp;

import com.davidhagar.gridphysics.util.RandomUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomExpressionTest {

    @Test
    @DisplayName("")
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

        assertEquals(-2, min);
        assertEquals(2, max);

    }

    @Test
    @DisplayName("")
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

        assertTrue(min > minLimit);
        assertTrue(max < maxLimit);


        assertEquals(minLimit, min, 1e-4);
        assertEquals(maxLimit, max, 1e-4);

    }

    @Test
    @DisplayName("")
    public void testGetRandomExpression() {

        RandomExpression re = new RandomExpression(2, 3, 999);

        for (int i = 0; i < 20; i++) {
            System.out.println(re.getRandomExpression());
        }
    }
}