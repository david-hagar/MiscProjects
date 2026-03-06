package com.davidhagar.serialdata.math;

import java.awt.*;

public class RotateProjector3D {
    private double cosRadX;
    private double sinRadX;
    private double cosRadY;
    private double sinRadY;
    private double xOffset2D;
    private double yOffset2D;
    double radX = Math.PI / 6;
    double radY = Math.PI / 6;
    double scale2D;
    double viewDistance;
    double windowCenterScaleAdjust;

    public RotateProjector3D(double radX, double radY, double scale2D, double viewDistance, double windowCenterScaleAdjust) {
        this.radX = radX;
        this.radY = radY;
        this.scale2D = scale2D;
        this.viewDistance = viewDistance;
        this.windowCenterScaleAdjust = windowCenterScaleAdjust;
        setRot(radX, radY);
     }

    public double getRadX() {
        return radX;
    }

    public double getRadY() {
        return radY;
    }

    public void setRot(double radX, double radY) {
        cosRadX = Math.cos(radX);
        sinRadX = Math.sin(radX);
        cosRadY = Math.cos(radY);
        sinRadY = Math.sin(radY);
    }

    public void projectPoint(double[] point3d, double[] point2d) {

        double x = point3d[0];
        double y = point3d[1];
        double z = point3d[2];

        double ny = y * cosRadX - z * sinRadX;
        double nz = y * sinRadX + z * cosRadX;
        y = ny;
        z = nz;

        double nx = x * cosRadY + z * sinRadY;
        nz = -x * sinRadY + z * cosRadY;
        x = nx;
        z = nz;


        double projectionFactor = viewDistance / (viewDistance + z);
        point2d[0] = (int) (x * projectionFactor * scale2D + viewDistance + xOffset2D);
        point2d[1] = (int) (y * projectionFactor * scale2D + viewDistance + yOffset2D);
    }


    public void centerOnWindow(Dimension windowDim) {
        xOffset2D = windowDim.width / 2.0;
        yOffset2D = windowDim.height / 2.0;

        scale2D = Math.min(windowDim.width , windowDim.height ) * windowCenterScaleAdjust;
    }
}
