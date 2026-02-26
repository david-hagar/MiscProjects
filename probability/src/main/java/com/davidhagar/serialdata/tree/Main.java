package com.davidhagar.serialdata.tree;

import com.davidhagar.serialdata.DisplayWindow;
import com.davidhagar.serialdata.math.RotateProjector3D;
import com.davidhagar.serialdata.math.Reduce4Dto3D;

public class Main {

    public static void main(String[] args) {
        TreePath treePath = new TreePath(3,10, 0.3, 0.8, 5);
        double[][] path = treePath.generate();

        RotateProjector3D p = new RotateProjector3D(Math.PI / 12, Math.PI / 6, 40, 5, 0.4);


        if( path[0].length == 4)
            path = Reduce4Dto3D.reduce(path);

        DisplayWindow.openFrame(path, p);
    }


}
