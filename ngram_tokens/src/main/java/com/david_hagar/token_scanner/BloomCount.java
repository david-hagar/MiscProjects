package com.david_hagar.token_scanner;

import java.util.BitSet;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BloomCount {


    int[] countBuffer = null;
    BitSet filter = null;
    private final int size;

    public BloomCount(int size) {

        countBuffer = new int[size];
        this.size = size;
    }


    public void addStringCount(String key, int count) {
        countBuffer[toHashIndex(key)] += count;
    }

    private int toHashIndex(String key) {
        return Math.abs(key.hashCode()) % size;
    }


    public void printLowCounts(int maxCount) {

        int c[] = new int[maxCount];

        for (int i = 0; i < size; i++) {
            if (countBuffer[i] < maxCount)
                c[countBuffer[i]]++;
        }

        for (int i = 0; i < maxCount; i++) {
            System.out.println(i + ", " + c[i]);
        }

    }


    private static class IndexedCount {
        int numberOfIndexes;
        int count;

        public IndexedCount(int numberOfIndexes, int count) {
            this.numberOfIndexes = numberOfIndexes;
            this.count = count;
        }
    }

    public void convertToFilter(int topN, float marginFraction) {

        PriorityQueue<Integer> q = new PriorityQueue<>(topN);

        q.add(countBuffer[0]);

        for (int i = 1; i < size; i++) {
            //noinspection ConstantConditions
            if (countBuffer[i] > q.peek()) {
                q.add(countBuffer[i]);
                if (q.size() > topN)
                    q.poll();
            }
        }

        System.out.println("count at top n = " + q.poll());
        int clipCount = Math.round(q.poll() * (1 - marginFraction));
        System.out.println("adjusted count at top n = " + clipCount);

        filter = new BitSet(size);
        for (int i = 1; i < size; i++) {
            filter.set(i, countBuffer[i] >= clipCount);
        }
        System.out.println("filter true fraction = " + filter.cardinality() / (float) size);

        countBuffer = null;
    }

    public boolean pass(String s) {
        return filter.get(toHashIndex(s));
    }


    public void printHighCounts(int maxCount) {

        PriorityQueue<Integer> q = new PriorityQueue<>(maxCount, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        q.add(countBuffer[0]);

        for (int i = 1; i < size; i++) {
            //noinspection ConstantConditions
            if (countBuffer[i] > q.peek()) {
                q.add(countBuffer[i]);
                if (q.size() > maxCount)
                    q.poll();
            }
        }

        while (!q.isEmpty()) {
            System.out.println(q.poll());
        }


    }


}
