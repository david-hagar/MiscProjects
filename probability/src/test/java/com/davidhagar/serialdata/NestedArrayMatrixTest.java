package com.davidhagar.serialdata;

import com.davidhagar.serialdata.gridwalk.NestedArrays;
import junit.framework.TestCase;

import java.util.Arrays;

public class NestedArrayMatrixTest extends TestCase {

    public void test() {

        NestedArrays<Boolean> a = new NestedArrays<Boolean>(2, 10);

        int[][] vList = {{2, 3}, {4, 5}, {2, 1}, {7, 7}, {9, 1}};

        for (int[] v : vList) {
            System.out.println(Arrays.toString(v));

            assertNull(a.get(v));

            a.set(v, true);
            assertTrue(a.get(v));
        }

    }

}