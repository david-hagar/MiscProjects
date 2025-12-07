package com.davidhagar.serialdata;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        final int nStepsPerDimension = 10;
        RandomAddGen rag = new RandomAddGen(3, nStepsPerDimension, 100);
        ArrayList<int[]> seq = rag.generate();

        DisplayWindow.openFrame(seq, nStepsPerDimension);

    }
}
