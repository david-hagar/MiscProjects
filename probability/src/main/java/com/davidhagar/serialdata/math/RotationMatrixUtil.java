package com.davidhagar.serialdata.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class RotationMatrixUtil {

    public static RealMatrix getRotationMatrix(int n, int dimA, int dimB, double angle) {
        double[][] matrix = new double[n][n];
// Initialize identity matrix
        for (int k = 0; k < n; k++) {
            matrix[k][k] = 1.0;
        }
        matrix[dimA][dimA] = Math.cos(angle);
        matrix[dimB][dimB] = Math.cos(angle);
        matrix[dimA][dimB] = -Math.sin(angle);
        matrix[dimB][dimA] = Math.sin(angle);

        return new Array2DRowRealMatrix(matrix);
    }


}
