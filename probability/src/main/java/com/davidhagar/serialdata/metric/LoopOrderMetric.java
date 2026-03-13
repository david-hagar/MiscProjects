package com.davidhagar.serialdata.metric;

import java.util.HashMap;

public class LoopOrderMetric {

    public static class OrderMetric {
        public final double averageOffset;
        public final double metric;

        public OrderMetric(double averageOffset, double metric) {
            this.averageOffset = averageOffset;
            this.metric = metric;
        }

        @Override
        public String toString() {
            return "OrderMetric{" +
                    "averageOffset=" + averageOffset +
                    ", metric=" + metric +
                    '}';
        }
    }


    public LoopOrderMetric(OrderMetric metricF, OrderMetric metricR, OrderMetric best) {
        this.metricF = metricF;
        this.metricR = metricR;
        this.best = best;
        this.metric = best.metric;
    }

    public final OrderMetric metricF;
    public final OrderMetric metricR;
    public final OrderMetric best;
    public final double metric;

    public static LoopOrderMetric measure(int[] idList1, int[] idList2) {
        OrderMetric metricF = measureForward(idList1, idList2);
        OrderMetric metricR = measureForward(idList1, reverseArray(idList2));
        OrderMetric best = metricF.metric < metricR.metric ? metricF : metricR;
        return new LoopOrderMetric(metricF, metricR, best);
    }

    private static OrderMetric measureForward(int[] idList1, int[] idList2) {

        if (idList1.length != idList2.length)
            throw new IllegalArgumentException("list1 and list2 must have same length");

        HashMap<Integer, Integer> map = new HashMap<>(idList1.length);

        for (int i = 0; i < idList1.length; i++)
            map.put(idList1[i], i);

        int totalOffset = 0;
        for (int i = 0; i < idList2.length; i++) {
            Integer otherIndex = map.get(idList2[i]);
            if (otherIndex == null)
                throw new IllegalArgumentException("id not found: " + idList2[i]);
            int offset = offsetPos(i, otherIndex, idList1.length);
            System.out.println(i + "=" + offset);
            totalOffset += offset;
        }
        double averageOffset = totalOffset / (double) idList1.length;

        double total = 0;
        for (int i = 0; i < idList2.length; i++) {
            Integer otherIndex = map.get(idList2[i]);
            if (otherIndex == null)
                throw new IllegalArgumentException("id not found: " + idList2[i]);
            double offset = offset(i, otherIndex, idList1.length) - averageOffset;
            total += offset * offset;
        }

        return new OrderMetric(averageOffset, total / idList1.length);
    }


    public static int offsetOld(int i1, int i2, int n) {
        int d = i1 - i2;
        if (d * 2 > n)
            d = -(n - d);
        if (d * 2 < -n)
            d = (n + d);
        return d;
    }


    public static int offset(int i1, int i2, int n) {

        int d = i1 - i2;
        int dnn = d - n;
        int dnp = d + n;

        int v = absMin(dnn, dnp, d);
        if (n % 2 == 0 && v == -n / 2)
            v = -v;

        return v;
    }

    public static int offsetPos(int i1, int i2, int n) {

        int d = i1 - i2;
        if (d < 0)
            d += n;

        return d;
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
        return "LoopOrderMetric{" +
                "\n\tmetricF=" + metricF +
                ", \n\tmetricR=" + metricR +
                ", \n\tbest=" + best +
                ", \n\tmetric=" + metric +
                '}';
    }
}
