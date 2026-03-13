package com.davidhagar.serialdata.metric;

import java.util.HashMap;

public class LoopOrderMetricB {

    public LoopOrderMetricB(double metric, boolean isReversed, int offset) {
        this.metric = metric;
        this.isReversed = isReversed;
        this.offset = offset;
    }

    public final double metric;
    public final boolean isReversed;
    public final int offset;


    public static LoopOrderMetricB measure(int[] idList1, int[] idList2) {

        int[] reverse2 = reverseArray(idList2);

        long bestTotal = Integer.MAX_VALUE;
        int bestIndex = 0;
        boolean bestIsReverse = false;

        for (int i = 0; i < idList1.length; i++) {
            long total = measureForward(idList1, idList2, i);
            long totalR = measureForward(idList1, reverse2, i);

            if (total < bestTotal) {
                bestTotal = total;
                bestIsReverse = false;
                bestIndex = i;
            }

            if (totalR < bestTotal) {
                bestTotal = totalR;
                bestIsReverse = true;
                bestIndex = i;
            }

            System.out.println(i + "= " + total + ", " + totalR);
        }

        double metric = bestTotal / (double) idList1.length;
        return new LoopOrderMetricB(metric, bestIsReverse, bestIndex);
    }

    private static long measureForward(int[] idList1, int[] idList2, int offset) {

        if (idList1.length != idList2.length)
            throw new IllegalArgumentException("list1 and list2 must have same length");

        HashMap<Integer, Integer> map = new HashMap<>(idList1.length);

        for (int i = 0; i < idList1.length; i++)
            map.put(idList1[i], i);

        long total = 0;
        for (int i = 0; i < idList2.length; i++) {
            int i2 = offset + i;
            if (i2 >= idList1.length)
                i2 -= idList1.length;
            Integer otherIndex = map.get(idList2[i2]);
            if (otherIndex == null)
                throw new IllegalArgumentException("id not found: " + idList2[i]);
            long d = diff(i, otherIndex, idList1.length);
            total += d * d;
        }


        return total;
    }

    public static int diff(int i1, int i2, int n) {

        int d = i1 - i2;
        int dnn = d - n;
        int dnp = d + n;

        int v = absMin(dnn, dnp, d);
        if (n % 2 == 0 && v == -n / 2)
            v = -v;

        return v;
    }

    public static int absMin(int i1, int i2, int i3) {

        int i1a = Math.abs(i1);
        int i2a = Math.abs(i2);
        int i3a = Math.abs(i3);

        boolean b1lt2 = i1a < i2a;
        boolean b2lt3 = i2a < i3a;
        boolean b1lt3 = i1a < i3a;

        if (b1lt2 && b1lt3)
            return i1;
        else if (b2lt3)
            return i2;
        else
            return i3;

    }

    public static int[] reverseArray(int[] originalArray) {
        int[] reversedArray = new int[originalArray.length];
        int lenM1 = originalArray.length - 1;
        for (int i = 0; i < originalArray.length; i++) {
            reversedArray[i] = originalArray[lenM1 - i];
        }
        return reversedArray;
    }


    @Override
    public String toString() {
        return "LoopOrderMetricB{" +
                "metric=" + metric +
                ", isReversed=" + isReversed +
                ", offset=" + offset +
                '}';
    }
}
