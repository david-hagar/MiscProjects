package com.davidhagar.serialdata.gradient_shift;

import com.davidhagar.serialdata.DisplayWindow;

public class Main {

    public static void main(String[] args) {
        final int nStepsPerDimension = 10;
        int nDim = 3;
        GradientShiftLoopPathGen g = new GradientShiftLoopPathGen(nDim, 0.25f, 20, false);
        double[][] v = g.generate();

        if( nDim == 4)
            v = Reduce4Dto3D.reduce(v);

        RotateProjector3D p = new RotateProjector3D(0, Math.PI / 6 , 1, 30);

        DisplayWindow.openFrame(v, nStepsPerDimension, p);

    }
}
