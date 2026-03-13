package com.davidhagar.serialdata.metric;

import junit.framework.TestCase;

import java.util.Arrays;

public class LoopOrderMetricBTest extends TestCase {

    public void testMeasure() {

        int[] list = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        testList(list, list, 0);

        int[] list2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        testList(list2, list2, 0);

        testList(list2, new int[]{3, 2, 1, 4, 5, 6, 7, 8, 9}, 8 / 9.0);

        testList(list, flipIndexArray(list, 0, 9), 2 / 10.0);
        testList(list, flipIndexArray(list, 1, 8), 18 / 10.0);

        testList2(list);
    }

    private static void testList(int[] list1, int[] list2, double expected) {

        for (int i = 0; i < list1.length; i++) {
            System.out.println("offset = " + i);
            int[] oList = offsetArray(list2, i);
            measure(list1, oList, expected);

            int[] rList = LoopOrderMetricB.reverseArray(oList);
            measure(list1, rList, expected);

            System.out.println();
        }
    }

    private static void testList2(int[] list) {

        for (int i = 0; i < list.length; i++) {
            System.out.println("offset = " + i);
            int[] oList = offsetArray(list, i);
            int[] rList = LoopOrderMetricB.reverseArray(oList);

            for (int j = 0; j < oList.length - 1; j++) {
                for (int k = j + 1; k < oList.length; k++) {

                    System.out.println("flip = " + j + ", " + k);
                    int d = LoopOrderMetricB.diff(j, k, list.length);
                    if (d == list.length / 2)
                        continue;
                    double expected = 2 * d * d / (double) list.length;

                    int[] ofList = flipIndexArray(oList, j, k);
                    measure(list, ofList, expected);

                    int[] rfList = flipIndexArray(rList, j, k);
                    measure(list, rfList, expected);
                }
            }


            System.out.println();
        }
    }


    private static void measure(int[] id1, int[] id2, double expected) {
        System.out.println("1: " + Arrays.toString(id1));
        System.out.println("2: " + Arrays.toString(id2));
        LoopOrderMetricB metric = LoopOrderMetricB.measure(id1, id2);
        System.out.println(metric);
        assertEquals(expected, metric.metric, 1e-6);
    }


    private static int[] offsetArray(int[] a, int i) {
        int[] result = new int[a.length];
        for (int j = 0; j < a.length; j++) {
            result[(j + i + a.length) % a.length] = a[j];
        }
        return result;
    }


    private static int[] flipIndexArray(int[] a, int i, int j) {
        int[] result = a.clone();

        i = clip(a, i);
        j = clip(a, j);
        int tmp = result[i];
        result[i] = result[j];
        result[j] = tmp;
        return result;
    }

    private static int clip(int[] a, int i) {
        if (i >= a.length)
            i -= a.length;
        else if (i < 0)
            i += a.length;
        return i;
    }

}