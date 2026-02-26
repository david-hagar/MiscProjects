package com.davidhagar.serialdata.gridwalk;

import com.davidhagar.serialdata.DisplayWindow;
import com.davidhagar.serialdata.math.Reduce4Dto3D;
import com.davidhagar.serialdata.math.RotateProjector3D;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        final int nStepsPerDimension = 10;
        RandomAddGen rag = new RandomAddGen(3, nStepsPerDimension, 100);
        ArrayList<double[]> seq = rag.generate();

        RotateProjector3D p = new RotateProjector3D(Math.PI / 12, Math.PI / 6, 1, 30, 0.25);

        double[][] convert = convert(seq);

        if( convert[0].length == 4)
            convert = Reduce4Dto3D.reduce(convert);


        DisplayWindow.openFrame(convert, p);

    }

    private static double[][] convert(ArrayList<double[]> seq) {
        double[][] v = new double[seq.size()][];
        for (int i = 0; i < seq.size(); i++) {
            v[i] = seq.get(i);
        }
        return v;
    }
}
