package com.davidhagar.serial;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


public class MatrixTest {


    public static void main(String[] args) {

        System.out.println(Nd4j.defaultFloatingPointType());
        //INDArray nd = Nd4j.create(new float[400],new int[]{2,200});
        int n = 300 * 1000 * 1000;
        long t1 = System.currentTimeMillis();
        INDArray nd1 = Nd4j.rand(new int[]{2, n});
        INDArray nd2 = Nd4j.rand(new int[]{2, n});
        //nd.putScalar(1,1);
        System.out.println("create = " + (System.currentTimeMillis() - t1));
        System.out.println(nd1);
        System.out.println(nd2);


        for (int i = 0; i < 3; i++) {
            t1 = System.currentTimeMillis();

            INDArray nd3 = nd1.mul(nd2);
            System.out.println("mult = " + (System.currentTimeMillis() - t1));
            System.out.println(nd3.dataType());
            System.out.println(nd3);

            nd3.close();

        }

    }

}

