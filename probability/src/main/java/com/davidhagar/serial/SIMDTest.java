package com.davidhagar.serial;



//import jdk.incubator.vector.IntVector;


/*
import jdk.incubator.vector.IntVector;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;


public class SIMDTest {

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};
        int[] c = new int[4];

        var species = IntVector.SPECIES_PREFERRED;
        for (int i = 0; i < a.length; i += species.length()) {
            var va = IntVector.fromArray(species, a, i);
            var vb = IntVector.fromArray(species, b, i);
            var vc = va.add(vb);
            vc.intoArray(c, i);
        }

        System.out.println(Arrays.toString(c)); // Output: [6, 8, 10, 12]
    }





}

*/