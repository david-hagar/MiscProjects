package com.davidhagar.serialdata;

import com.davidhagar.serialdata.math.RotationMatrixUtil;
import junit.framework.TestCase;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.apache.commons.math3.util.CompositeFormat;

public class RotationMatrixUtilTest extends TestCase {

    public void testGetRotationMatrix() {

        double[][] a = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        RealMatrix data = new Array2DRowRealMatrix(a);

        RealMatrix r1 = RotationMatrixUtil.getRotationMatrix(3, 1, 2, Math.PI / 2);
        RealMatrix r2 = RotationMatrixUtil.getRotationMatrix(3, 2, 1, -Math.PI / 2);

        RealMatrix res1 = r1.multiply(data);
        RealMatrix res2 = r2.multiply(data);

        RealMatrixFormat format = new RealMatrixFormat("{", "}", "{", "}", ",\r\n", ",", CompositeFormat.getDefaultNumberFormat());

        System.out.println(format.format(r1));
        System.out.println();
        System.out.println(format.format(r2));
        System.out.println();

        System.out.println(format.format(data));
        System.out.println();
        System.out.println(format.format(res1));
        System.out.println();
        System.out.println(format.format(res2));
    }
}