package com.davidhagar.serialdata.tree;

import com.davidhagar.serialdata.DisplayWindow;
import com.davidhagar.serialdata.SmoothingGenerators;
import com.davidhagar.serialdata.math.RotateProjector3D;
import com.davidhagar.serialdata.math.Reduce4Dto3D;

public class Main {

    public static void main(String[] args) {
        TreePath treePath = new TreePath(3,10, 0.1,
                0.76, .005, 11);
        double[][] path = treePath.generate();

        RotateProjector3D p = new RotateProjector3D(Math.PI/10, 0, 40, 5, 0.35);

        for (int i = 0; i < 3; i++) {
            path = SmoothingGenerators.addMidpoints(path);
            path = SmoothingGenerators.smooth(path, 0.8);
        }

        if( path[0].length == 4)
            path = Reduce4Dto3D.reduce(path);

        DisplayWindow.openFrame(path, p);
    }


}
