package com.davidhagar.serialdata;

import junit.framework.TestCase;

import java.util.Arrays;

import static com.davidhagar.serialdata.LoopOrderMetric.*;
import static org.junit.Assert.assertArrayEquals;


public class LoopOrderMetricTest extends TestCase {


    public void testMeasure2() {

        int[] id1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] id2 = {8, 9, 1, 2, 3, 4, 5, 6, 7};

        assertArrayEquals(id2, offsetArray(id1, 2));

        for (int i = 0; i < id1.length; i++) {
            System.out.println(i);
            int[] idO = offsetArray(id1, i);
            measureFlip(id1, idO, 0.0);
            int[] idR = reverseArray(idO);
            measureFlip(id1, idR, 0.0);
            System.out.println(-i);
            int[] idn = offsetArray(id1, -i);
            measureFlip(id1, idn, 0.0);
            int[] idnR = reverseArray(idn);
            measureFlip(id1, idnR, 0.0);

        }

    }

    private int[] offsetArray(int[] a, int i) {
        int[] result = new int[a.length];
        for (int j = 0; j < a.length; j++) {
            result[(j + i + a.length) % a.length] = a[j];
        }
        return result;
    }


    public void testMeasure3() {
        int[] id1 = {1, 2, 3};
        int[] id1R = {2, 3, 1};
        int[] id1R2 = {3, 1, 2};

        int[] id2 = {1, 2, 3};
        int[] id3 = {1, 3, 2};
        int[] id4 = {3, 2, 1};

        int[] id1RA = reverseArray(id1);
        assertArrayEquals(id4, id1RA);

        measure(id1, id2, 0.0);
        measure(id1, id1R, 0.0);
        measure(id1, id1R2, 0.0);
        measure(id1, id3, 0);
        measure(id1, id4, 0);
    }

    public void testMeasure1() {
        {
            int[] idA = {1, 2, 3, 4};
            int[] idB = {1, 2, 4, 3};
            int[] idC = {2, 1, 3, 4};
            measure(idA, idB, 0.25);
            measure(idA, idC, 0.25);
        }
        {
            int[] idA = {1, 2, 3, 4, 5};
            int[] idB = {1, 2, 4, 3, 5};
            int[] idC = {2, 1, 3, 4, 5};
            measure(idA, idB, 0.16);
            measure(idA, idC, 0.24);
        }
    }


    public void testOffset() {
        int n = 10;
        assertEquals(0, offset(1, 1, n));
        assertEquals(-1, offset(1, 2, n));
        assertEquals(+1, offset(2, 1, n));

        assertEquals(-1, offset(4, 5, n));
        assertEquals(+1, offset(5, 4, n));


        assertEquals(+1, offset(0, 9, n));
        assertEquals(-1, offset(9, 0, n));

        assertEquals(+3, offset(1, 8, n));
        assertEquals(-3, offset(8, 1, n));

        assertEquals(-1, offset(8, 9, n));
        assertEquals(+1, offset(9, 8, n));

        assertEquals(+5, offset(6, 1, n));
        assertEquals(+5, offset(1, 6, n));

        assertEquals(+5, offset(6, 1, n + 1));
        assertEquals(-5, offset(1, 6, n + 1));


        for (int i = 0; i < n - 1; i++) {
            int i2 = (i + 1) % n;
            System.out.println(i + ", " + i2);
            assertEquals(-1, offset(i, i2, n));
            assertEquals(+1, offset(i2, i, n));

        }
        System.out.println();

        for (int i = 0; i < 2; i++) {
            int i2 = (9 - i);
            System.out.println(i + ", " + i2);
            int i3 = i * 2 + 1;
            assertEquals(i3, offset(i, i2, n));
            assertEquals(-i3, offset(i2, i, n));
        }


    }

    private static void measureFlip(int[] id1, int[] id2, double expected) {
        System.out.println("f");
        measure(id1, id2, expected);
        System.out.println("r");
        measure(id2, id1, expected);
    }

    private static void measure(int[] id1, int[] id2, double expected) {
        System.out.println("1: " + Arrays.toString(id1));
        System.out.println("2: " + Arrays.toString(id2));
        LoopOrderMetric metric = LoopOrderMetric.measure(id1, id2);
        System.out.println(metric);
        assertEquals(expected, metric.metric, 1e-6);
    }

    public void testAbsMin() {
        testNeg1(1, 2, 3);
        testNeg1(1, 3, 2);
        testNeg3(3, 2, 1);
        testNeg2(3, 1, 2);
        testNeg3(2, 3, 1);
        testNeg2(2, 1, 3);
    }

    private static void testNeg1(int i1, int i2, int i3) {
        assertEquals(i1, absMin(i1, i2, i3));
        assertEquals(-i1, absMin(-i1, i2, i3));
        assertEquals(i1, absMin(i1, -i2, i3));
        assertEquals(i1, absMin(i1, i2, -i3));
    }

    private static void testNeg2(int i1, int i2, int i3) {
        assertEquals(i2, absMin(i1, i2, i3));
        assertEquals(i2, absMin(-i1, i2, i3));
        assertEquals(-i2, absMin(i1, -i2, i3));
        assertEquals(i2, absMin(i1, i2, -i3));
    }

    private static void testNeg3(int i1, int i2, int i3) {
        assertEquals(i3, absMin(i1, i2, i3));
        assertEquals(i3, absMin(-i1, i2, i3));
        assertEquals(i3, absMin(i1, -i2, i3));
        assertEquals(-i3, absMin(i1, i2, -i3));
    }

}