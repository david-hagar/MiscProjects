package com.davidhagar.serialdata.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class MatrixUtil {

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


    public static RealMatrix createTranslationMatrix(double[] translationVector) {

        int dimensions = translationVector.length;
        int matrixSize = dimensions + 1;
        double[][] matrix = new double[matrixSize][matrixSize];

        // Initialize as an identity matrix
        for (int i = 0; i < matrixSize; i++) {
            matrix[i][i] = 1.0;
        }

        // Place the translation vector elements in the last column
        for (int i = 0; i < dimensions; i++) {
            matrix[i][dimensions] = translationVector[i];
        }

        return MatrixUtils.createRealMatrix(matrix);
    }


    public static void print(double[][] matrix) {


        // Format specifier:
        // %: indicates the start of a format specifier
        // 8: minimum field width of 8 characters
        // .: separator
        // 2: number of decimal places (precision)
        // f: format specifier for a floating-point number
        String formatSpecifier = "%8.2f";

        for (int i = 0; i < matrix.length; i++) { // Loop through rows
            //System.out.print("}");
            for (int j = 0; j < matrix[i].length; j++) { // Loop through columns
                System.out.printf(formatSpecifier, matrix[i][j]);
                if(j < matrix[i].length - 1)
                    System.out.print(", ");
            }
            System.out.println(""); // Move to the next line after each row
        }
        System.out.println();
    }


}
