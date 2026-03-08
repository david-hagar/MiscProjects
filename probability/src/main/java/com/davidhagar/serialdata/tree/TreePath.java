package com.davidhagar.serialdata.tree;

import com.davidhagar.serialdata.math.MatrixUtil;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public class TreePath {
    //private static final RealMatrixFormat format = new RealMatrixFormat("{", "}", "{", "}", ",\r\n", ",", CompositeFormat.getDefaultNumberFormat());

    private final RealMatrix points;
    private final RealMatrix scale;
    private final int nDim;
    private final double rootLength;
    private final double sideOffsetFraction;
    private final double stopDistance;
    private final int depthLimit;
    private final RealMatrix plus90Rot;
    private final RealMatrix minus90Rot;
    private final RealMatrix translate;

    public TreePath(int nDim, double rootLength, double sideOffsetFraction, double lengthShortenFraction, double stopDistance, int depthLimit) {
        this.nDim = nDim;
        this.rootLength = rootLength;
        this.sideOffsetFraction = sideOffsetFraction;
        this.stopDistance = stopDistance;
        this.depthLimit = depthLimit;

        int nDimP1 = nDim + 1;

        plus90Rot = MatrixUtil.getRotationMatrix(nDimP1, 0, 1, Math.PI / 2).multiply(
                MatrixUtil.getRotationMatrix(nDimP1, 1, 2, Math.PI / 2)
        );
        minus90Rot = MatrixUtil.getRotationMatrix(nDimP1, 0, 1, -Math.PI / 2).multiply(
                MatrixUtil.getRotationMatrix(nDimP1, 1, 2, Math.PI / 2)
        );;
//        plus90Rot = new RealMatrix[nDim];
//        minus90Rot = new RealMatrix[nDim];
//        for (int i = 0; i < nDim; i++) {
//            int ip1 = (i + 1) % nDim;
//            plus90Rot[i] = MatrixUtil.getRotationMatrix(nDimP1, i, ip1, Math.PI / 2);
//            minus90Rot[i] = MatrixUtil.getRotationMatrix(nDimP1, i, ip1, -Math.PI / 2);
//
////            System.out.println(i);
////            System.out.println(format.format(plus90Rot[i]));
////            System.out.println(format.format(minus90Rot[i]));
////            System.out.println();
//        }

        points = MatrixUtils.createRealMatrix(nDimP1, 3);
        double offset = rootLength * sideOffsetFraction;
        points.setEntry(0, 0, rootLength - offset);
        points.setEntry(1, 0, +offset);
        points.setEntry(0, 1, rootLength + offset);
        points.setEntry(1, 1, 0);
        points.setEntry(0, 2, rootLength - offset);
        points.setEntry(1, 2, -offset);

        for (int i = 0; i < 3; i++)
            points.setEntry(nDim, i, 1);

        scale = MatrixUtils.createRealIdentityMatrix(nDimP1).scalarMultiply(lengthShortenFraction);
        scale.setEntry(nDim, nDim, 1);

        double[] t = new double[nDim];
        t[0] = rootLength;
        translate = MatrixUtil.createTranslationMatrix(t);

        System.out.println("scale");
        MatrixUtil.print(scale.getData());
        System.out.println("translate");
        MatrixUtil.print(translate.getData());

    }

    public double[][] generate() {
        ArrayList<double[]> list = new ArrayList<>();

        double sideOffsetLen = sideOffsetFraction * rootLength;
        int nDimP1 = nDim + 1;
        double[] v1 = new double[nDimP1];
        v1[1] = sideOffsetLen;
        v1[nDim] = 1;
        list.add(v1);

        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(nDimP1);
        generateR(list, 0, identity, 0);

        double[] v2 = new double[nDimP1];
        v2[1] = -sideOffsetLen;
        v2[nDim] = 1;
        list.add(v2);

        double[][] values = new double[list.size()][nDim];
        for (int i = 0; i < list.size(); i++) {
            double[] v = list.get(i);
            double[] vNew = new double[nDim];
            System.arraycopy(v, 0, vNew, 0, nDim);
            values[i] = vNew;
        }

        return values;
    }

    private void generateR(ArrayList<double[]> list, int dimIndex, RealMatrix trMatrix, int depth) {
        int dimIndexNext = (dimIndex + 1 + nDim) % nDim;
        //System.out.println(dimIndex + " " + dimIndexNext);
        //MatrixUtil.print(trMatrix.getData());
        //System.out.println(format.format(trMatrix));

        RealMatrix newPoints = trMatrix.multiply(points);

        double[] br = newPoints.getColumn(0);
        double[] tm = newPoints.getColumn(1);
        double[] bl = newPoints.getColumn(2);

        double m = magSqrDiff(tm, list.getLast());
        if (m < stopDistance * stopDistance || depth++ >= depthLimit)
            return;

        RealMatrix baseMatrix = trMatrix.multiply(translate).multiply(scale);

        list.add(br);
        RealMatrix right = baseMatrix.multiply(plus90Rot);
        generateR(list, dimIndexNext, right, depth);

        list.add(tm);
        RealMatrix left = baseMatrix.multiply(minus90Rot);
        generateR(list, dimIndexNext, left, depth);
        list.add(bl);

    }

    private static double magSqr(double[] v) {
        double total = 0;
        for (double e : v) total += e * e;
        return total;
    }

    private static double magSqrDiff(double[] v1, double[] v2) {
        double total = 0;
        for (int i = 0; i < v1.length; i++) {
            double e = v1[i] - v2[i];
            total += e * e;
        }
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
