package com.davidhagar.serialdata.gradient_shift;

import com.davidhagar.serialdata.DisplayWindow;

public class Main {

    public static void main(String[] args) {
        final int nStepsPerDimension = 10;
        GradientShiftLoopPathGen g = new GradientShiftLoopPathGen(3, 0.25f, 20, true);
        double[][] v = g.generate();
        RotateProjector3D p = new RotateProjector3D(0, Math.PI / 6 , 1, 30);

        DisplayWindow.openFrame(v, nStepsPerDimension, p);

    }
}
