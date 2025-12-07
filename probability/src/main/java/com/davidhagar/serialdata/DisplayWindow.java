package com.davidhagar.serialdata;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;

public class DisplayWindow extends JPanel {

    private final ArrayList<int[]> values;
    private final int maxBounds;


    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    public DisplayWindow(ArrayList<int[]> values, int maxBounds) {
        this.values = values;
        this.maxBounds = maxBounds;
        this.setBackground(Color.BLACK);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHints(rh);

//        g2d.setColor(Color.GRAY);
//        g2d.drawLine(0,0, 100,100);

        if (values.get(0).length == 2)
            paint2D(g2d);

        if (values.get(0).length == 3)
            paint3D(g2d);

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

    public static void projectPoint(int[] point3d, int[] point2d, double distance) {

        double x = point3d[0];
        double y = point3d[1];
        double z = point3d[2];

        double radX = Math.PI / 6;
        double ny = y * Math.cos(radX) - z * Math.sin(radX);
        double nz = y * Math.sin(radX) + z * Math.cos(radX);
        y=ny;
        z=nz;

        double radY = Math.PI / 6;
        double nx = x * Math.cos(radY) + z * Math.sin(radY);
        nz = -x * Math.sin(radY) + z * Math.cos(radY);
        x=nx;
        z=nz;

        double scale = 60;
        double offset = 300;

        double projectionFactor = distance / (distance + z);
        point2d[0] = (int) (x * projectionFactor * scale + offset);
        point2d[1] = (int) (y * projectionFactor * scale + offset);
    }


    private void paint3D(Graphics2D g2d) {
        Dimension size = this.getSize();

        int[] point2d = {0, 0};
        int[] last2d = {0, 0};

        //g2d.drawLine(-100,-100,100,100);
        //g2d.fillRect(100, 100, 10, -10);

        projectPoint(values.get(values.size() - 1), last2d, 10);
        for (int i = 0; i < values.size(); i++) {
            int[] v = values.get(i);

            projectPoint(v, point2d, 30);

            float hue = i/(float)values.size();
//            g2d.setColor(Color.GRAY);
            g2d.setColor(Color.getHSBColor(hue, 1, 1));
            g2d.drawLine(point2d[0], point2d[1], last2d[0], last2d[1]);

            //g2d.setColor(Color.RED);
            //g2d.fillRect((int) (v[0] *s), (int) (v[1]*-s), 4, 4);

            //g2d.drawString(Arrays.toString(v), (int) (v[0] *s), (int) (v[1]*-s));
            int[] tmp = last2d;
            last2d = point2d;
            point2d = tmp;

            //System.out.println(Arrays.toString(point2d));
        }
    }

    private void paint2D(Graphics2D g2d) {
        Dimension size = this.getSize();
        //double barWidth = size.width / (double) values.length;
        int margin = 20;

        AffineTransform t = g2d.getTransform();
        //AffineTransform tCopy = (AffineTransform) t.clone();
        final int tx = margin;
        final int ty = size.height - margin;
        t.translate(tx, ty);
        int minSize = Math.min(size.width, size.height);
        final double s = (minSize - margin * 2) / ((double) maxBounds);
        //t.scale(s, -s);
        g2d.setColor(Color.GRAY);
        g2d.setTransform(t);

        //g2d.drawLine(-100,-100,100,100);
        //g2d.fillRect(100, 100, 10, -10);

        int[] last = values.get(values.size() - 1);
        for (int[] v : values) {
            //System.out.println(Arrays.toString(v));
            g2d.setColor(Color.GRAY);
            g2d.drawLine((int) (v[0] * s), (int) (v[1] * -s), (int) (last[0] * s), (int) (last[1] * -s));

            //g2d.setColor(Color.RED);
            //g2d.fillRect((int) (v[0] *s), (int) (v[1]*-s), 4, 4);

            //g2d.drawString(Arrays.toString(v), (int) (v[0] *s), (int) (v[1]*-s));
            last = v;
        }
    }


    public static void openFrame(ArrayList<int[]> values, int maxBounds) {
        JFrame frame = new JFrame("Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DisplayWindow(values, maxBounds));
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