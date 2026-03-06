package com.davidhagar.serialdata;


import com.davidhagar.serialdata.math.RotateProjector3D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DisplayWindow extends JPanel {

    private final double[][] values;
    private final RotateProjector3D p;
    private double rotatingAngle = 0;
    private long lastRepaintTime = System.currentTimeMillis();


    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    public DisplayWindow(double[][] values, RotateProjector3D p) {
        this.values = values;
        this.p = p;
        this.setBackground(Color.BLACK);

        centerValues();
    }

    private void centerValues() {
        double[] average = new double[values[0].length];
        for (double[] value : values) {
            for (int i = 0; i < value.length; i++) {
                average[i] += value[i];
            }
        }

        for (int i = 0; i < average.length; i++) {
            average[i] /= values.length;
        }

        for (double[] value : values) {
            for (int j = 0; j < value.length; j++) {
                value[j] -= average[j];
            }
        }

        double max = 0;
        for (double[] value : values) {
            for (int i = 0; i < value.length; i++) {
                if(value[i] > max) {
                    max = value[i];
                }
            }
        }

        for (double[] value : values) {
            for (int j = 0; j < value.length; j++) {
                value[j] /= max;
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHints(rh);

//        g2d.setColor(Color.GRAY);
//        g2d.drawLine(0,0, 100,100);

        if (values[0].length == 2)
            paint2D(g2d);

        else if (values[0].length == 3)
            paint3D(g2d);
        else throw new IllegalArgumentException("values.length != 2 or 3");

    }

/*

    public void rotateX(double angle) {
        double rad = Math.toRadians(angle);
        double newY = y * Math.cos(rad) - z * Math.sin(rad);
        double newZ = y * Math.sin(rad) + z * Math.cos(rad);
        y = newY;
        z = newZ;
    }

    public void rotateY(double angle) {
        double rad = Math.toRadians(angle);
        double newX = x * Math.cos(rad) + z * Math.sin(rad);
        double newZ = -x * Math.sin(rad) + z * Math.cos(rad);
        x = newX;
        z = newZ;
    }
*/


    private void paint3D(Graphics2D g2d) {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastRepaintTime;
        lastRepaintTime = now;

        rotatingAngle -= Math.PI * elapsedMillis /1000.0/50;

        double[] point2d = {0, 0};
        double[] last2d = {0, 0};

        //g2d.drawLine(-100,-100,100,100);
        //g2d.fillRect(100, 100, 10, -10);

        Dimension windowDim = this.getSize();
        p.centerOnWindow(windowDim);

        p.projectPoint(values[values.length - 1], last2d);
        for (int i = 0; i < values.length; i++) {
            double[] v = values[i];

            p.setRot(p.getRadX(), rotatingAngle);
            p.projectPoint(v, point2d);

            float hue = i / (float) values.length;
//            g2d.setColor(Color.GRAY);
            g2d.setColor(Color.getHSBColor(hue, 1, 1));
            g2d.drawLine((int) point2d[0], (int) point2d[1], (int) last2d[0], (int) last2d[1]);

            //g2d.setColor(Color.RED);
            //g2d.fillRect((int) (v[0] *s), (int) (v[1]*-s), 4, 4);

            //g2d.drawString(Arrays.toString(v), (int) (v[0] *s), (int) (v[1]*-s));
            double[] tmp = last2d;
            last2d = point2d;
            point2d = tmp;

            //System.out.println(Arrays.toString(point2d));
        }
        this.repaint();
    }

    private void paint2D(Graphics2D g2d) {
        Dimension size = this.getSize();
        //double barWidth = size.width / (double) values.length;
        int margin = 20;

        AffineTransform t = g2d.getTransform();
        //AffineTransform tCopy = (AffineTransform) t.clone();
        t.translate(size.width/2f, size.height/2f);
        int minWinDim = Math.min(size.width, size.height);
        final double s = (minWinDim/2f - margin * 2) ;
        //t.scale(s, -s);
        g2d.setColor(Color.GRAY);
        g2d.setTransform(t);

        //g2d.drawLine(-100,-100,100,100);
        //g2d.fillRect(100, 100, 10, -10);

        double[] last = values[values.length - 1];
        for (double[] v : values) {
            //System.out.println(Arrays.toString(v));
            g2d.setColor(Color.GRAY);
            g2d.drawLine((int) (v[0] * s), (int) (v[1] * -s), (int) (last[0] * s), (int) (last[1] * -s));

            //g2d.setColor(Color.RED);
            //g2d.fillRect((int) (v[0] *s), (int) (v[1]*-s), 4, 4);

            //g2d.drawString(Arrays.toString(v), (int) (v[0] *s), (int) (v[1]*-s));
            last = v;
        }
    }


    public static void openFrame(double[][] values, RotateProjector3D p) {
        JFrame frame = new JFrame("Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DisplayWindow(values, p));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int margin = 100;
        screenSize.height -= margin * 2;
        screenSize.width -= margin * 2;
        frame.setSize(screenSize);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);
        frame.setLocation(margin, margin);
    }
}