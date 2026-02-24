package com.davidhagar.serialdata.gradient_shift;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class Reduce4Dto3D {
    public static double[][] reduce(double[][] data) {
        // Assume data[N][4] is your 4D data
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        // 1. Center and 2. Covariance
        RealMatrix covariance = new Covariance(matrix).getCovarianceMatrix();

        // 3. Eigen Decomposition
        EigenDecomposition ed = new EigenDecomposition(covariance);

        // 4. Get top 3 Eigenvectors (components)
        // EigenDecomposition orders eigenvalues descending by default
        RealMatrix eigenVectors = ed.getV();
        RealMatrix projectionMatrix = eigenVectors.getSubMatrix(0, 3, 0, 2);

        // 5. Reduce: 4D to 3D (Multiply N*4 by 4*3)
        RealMatrix reducedData = matrix.multiply(projectionMatrix);

        return reducedData.getData(); // now contains N*3 data
    }
}