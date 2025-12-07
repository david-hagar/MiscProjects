package com.davidhagar.serialdata;

import java.util.*;
import java.util.stream.IntStream;

public class RandomAddGen {

    private final int[] dimIndexes;
    private final NestedArrays na;
    private final Random rand;
    private int nDim;
    private int maxDim;
    private int maxIterations;


    public RandomAddGen(int nDim, int maxDim, int maxIterations) {
        this.nDim = nDim;
        this.maxDim = maxDim;

        dimIndexes = IntStream.range(0, nDim).toArray();
        this.maxIterations = maxIterations;
        //shuffleArray(dimIndexes);

        na = new NestedArrays(nDim, maxDim);

        rand = new Random();
    }


    private static void shuffleArray(int[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    public ArrayList<int[]> generate() {

        ArrayList<int[]> sequence = new ArrayList<>();

        int middle = maxDim / 2;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int[] v = new int[nDim];
                v[0] = i + middle;
                v[1] = (i == 0 ? j : 1 - j) + middle;
                sequence.add(v);
                //System.out.println("v=" + Arrays.toString(v));
                na.set(v, true);

            }
        }
        System.out.println();

        for (int i = 0; i < maxIterations; i++) {
            ArrayList<int[]> e = expand(sequence);
            if (e.size() == sequence.size()) {
                System.out.println("break");
                break;
            }
            Collections.reverse(e);
            sequence = e;
            shuffleArray(dimIndexes);
        }


        return sequence;
    }

    private ArrayList<int[]> expand(ArrayList<int[]> sequence) {

        //int limit = 200;

        ArrayList<int[]> newSeq = new ArrayList<>();

        int[] last = sequence.get(sequence.size() - 1);
        for (int[] v : sequence) {
            //if (limit-- <= 0)
            //    newSeq.add(v);
            //else {

            int[][] offset = getOpenOffset(last, v);
            if (offset != null) {
                newSeq.add(offset[0]);
                newSeq.add(offset[1]);
                newSeq.add(v);


//                    System.out.println("f=" + Arrays.toString(last));
//                    System.out.println("s=" + Arrays.toString(v));
//                    System.out.println("0=" + Arrays.toString(offset[0]) + na.get(offset[0]));
//                    System.out.println("1=" + Arrays.toString(offset[1]) + na.get(offset[1]));
//                    System.out.println();

                na.set(offset[0], true);
                na.set(offset[1], true);
            } else
                newSeq.add(v);
            //}
            last = v;
        }
        return newSeq;
    }

    private int[][] getOpenOffset(int[] first, int[] second) {
        int[] offset = new int[first.length];
        int[] tmp1 = new int[first.length];
        int[] tmp2 = new int[first.length];

        for (int index : dimIndexes) {
            int sign = rand.nextBoolean() ? -1 : 1;
            for (int i = -1; i <= 1; i += 2) {
                System.arraycopy(first, 0, tmp1, 0, first.length);
                tmp1[index] += i * sign;
                // System.out.println("tmp1=" + Arrays.toString(tmp1) + " na=" + na.get(tmp1));
                if (!inBounds(tmp1) || na.get(tmp1) != null)
                    continue;

                System.arraycopy(second, 0, tmp2, 0, second.length);
                tmp2[index] += i * sign;
                //System.out.println("tmp2=" + Arrays.toString(tmp2) + " na=" + na.get(tmp2));
                if (inBounds(tmp2) && na.get(tmp2) == null) {
                    return new int[][]{tmp1, tmp2};
                }
            }
        }

        return null;
    }

    private boolean inBounds(int[] v) {
        for (int i : v) {
            if (i < 0 || i >= maxDim)
                return false;
        }
        return true;
    }

}
