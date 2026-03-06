package com.davidhagar.serialdata.tree;

import com.davidhagar.serialdata.math.RotationMatrixUtil;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.apache.commons.math3.util.CompositeFormat;

import java.util.ArrayList;

public class TreePath {
    private static final RealMatrixFormat format = new RealMatrixFormat("{", "}", "{", "}", ",\r\n", ",", CompositeFormat.getDefaultNumberFormat());

    private final RealMatrix points;
    private final RealMatrix scale;
    private final int nDim;
    private final double rootLength;
    private final double sideOffsetFraction;
    private final double stopDistance;
    private final RealMatrix plus90Rot[];
    private final RealMatrix minus90Rot[];

    public TreePath(int nDim, double rootLength, double sideOffsetFraction, double lengthShortenFraction, double stopDistance) {
        this.nDim = nDim;
        this.rootLength = rootLength;
        this.sideOffsetFraction = sideOffsetFraction;
        this.stopDistance = stopDistance;

        plus90Rot = new RealMatrix[nDim];
        minus90Rot = new RealMatrix[nDim];
        for (int i = 0; i < nDim; i++) {
            int ip1 = (i + 1) % nDim;
            plus90Rot[i] = RotationMatrixUtil.getRotationMatrix(nDim, i, ip1, Math.PI / 2);
            minus90Rot[i] = RotationMatrixUtil.getRotationMatrix(nDim, i, ip1, -Math.PI / 2);

//            System.out.println(i);
//            System.out.println(format.format(plus90Rot[i]));
//            System.out.println(format.format(minus90Rot[i]));
//            System.out.println();
        }

        points = MatrixUtils.createRealMatrix(nDim, 4);
        double offset = rootLength * sideOffsetFraction;
        points.setEntry(0, 0, rootLength - offset);
        points.setEntry(1, 0, +offset);
        points.setEntry(0, 1, rootLength + offset);
        points.setEntry(1, 1, 0);
        points.setEntry(0, 2, rootLength - offset);
        points.setEntry(1, 2, -offset);

        points.setEntry(0, 3, rootLength);

        scale = MatrixUtils.createRealIdentityMatrix(nDim).scalarMultiply(lengthShortenFraction);
    }

    public double[][] generate() {
        ArrayList<double[]> list = new ArrayList<>();

        double sideOffsetLen = sideOffsetFraction * rootLength;
        double[] v1 = new double[nDim];
        v1[1] = sideOffsetLen;
        list.add(v1);

        double[] root = new double[nDim];

        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(nDim);
        generateR(list, 0, root, identity);

        double[] v2 = new double[nDim];
        v2[1] = -sideOffsetLen;
        list.add(v2);

        double[][] values = new double[list.size()][nDim];
        for (int i = 0; i < list.size(); i++)
            values[i] = list.get(i);


        return values;
    }

    private void generateR(ArrayList<double[]> list, int dimIndex, double[] root, RealMatrix rotMatrix) {
        int dimIndexP1 = (dimIndex + 1 + nDim) % nDim;
        System.out.println(dimIndex + " " + dimIndexP1);
        System.out.println(format.format(rotMatrix));

        RealMatrix newPoints = rotMatrix.multiply(points);
        double[] rootLengthOffset = newPoints.getColumn(3);
        double[] newRoot = add(rootLengthOffset, root);
        double m = magSqr(rootLengthOffset);
        if (m < stopDistance * stopDistance)
            return;

        double[] br = add(newPoints.getColumn(0), root);
        double[] tm = add(newPoints.getColumn(1), root);
        double[] bl = add(newPoints.getColumn(2), root);

        RealMatrix scaledRotMatrix = scale .multiply(rotMatrix);

        list.add(br);
        RealMatrix right = plus90Rot[dimIndex].multiply(scaledRotMatrix);
        generateR(list, dimIndexP1, newRoot, right);

        list.add(tm);
        RealMatrix left = minus90Rot[dimIndex].multiply(scaledRotMatrix);
        generateR(list, dimIndexP1, newRoot, left);
        list.add(bl);

    }

    private static double magSqr(double[] v) {
        double total = 0;
        for (double e : v) total += e * e;
        return total;
    }

    private static void add(double[] v1, double[] v2, double[] dest) {
        for (int i = 0; i < dest.length; i++) dest[i] = v1[i] + v2[i];
    }

    private static void neg(double[] v) {
        for (int i = 0; i < v.length; i++) v[i] = -v[i];
    }

    private static double[] add(double[] v1, double[] v2) {
        double[] dest = new double[v1.length];
        for (int i = 0; i < v1.length; i++) dest[i] = v1[i] + v2[i];
        return dest;
    }

    private static void addTo(double[] v1, double[] v2) {
        for (int i = 0; i < v1.length; i++) v1[i] += v2[i];
    }

    private static double[] clone(double[] v) {
        double[] dest = new double[v.length];
        System.arraycopy(v, 0, dest, 0, v.length);
        return dest;
    }


    /*


        // base 1
        double sideOffsetLen = sideOffsetFraction * length;
        {
            double[] sideOffsetA = clone(root);
            sideOffsetA[dimIndex] += sideOffsetLen;
            sideOffsetA[dimIndexP1] += sideOffsetLen;
            list.add(sideOffsetA);
        }

        // end 1

        double[] endRoot = clone(root);
        double newLength = length * lengthShortenFraction;
        endRoot[dimIndexP1] += newLength;

        double[] endPath = clone(endRoot);
        endPath[dimIndex] -= sideOffsetLen;

        {
            double[] endA = clone(endPath);
            double[] endB = clone(endPath);
            endA[dimIndex] += sideOffsetLen;
            endB[dimIndex] -= sideOffsetLen;
            list.add(endA);

            generateR(list, dimIndexP1, endPath, newLength, rotMatrix);

            list.add(endB);
        }

        {
            double[] sideOffsetB = clone(root);
            sideOffsetB[dimIndex] -= sideOffsetLen;
            sideOffsetB[dimIndexP1] += sideOffsetLen;
            list.add(sideOffsetB);
        }
     */


}
