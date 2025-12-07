package com.davidhagar.prob;


import javax.swing.*;
import java.awt.*;

public class DisplayWindow extends JPanel {

    int maxIndex = -1;
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    private double[][] values;

    public DisplayWindow(double[][] values) {
        this.values = values;
        this.setBackground(Color.BLACK);
    }

    public void setValues(double[][] values) {
        this.values = values;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (values == null)
            return;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHints(rh);

        Dimension size = this.getSize();
        //double barWidth = size.width / (double) values.length;
        double maxY = maxY();
        int margin = 20;

        //AffineTransform t = g2d.getTransform();
        //AffineTransform tCopy = (AffineTransform) t.clone();
        final int ty = size.height - margin;
        //t.translate(margin, ty);
        final double sx = (size.width - margin * 2) / (double) values.length;
        final double sy = -(size.height - margin * 2) / maxY;
        //t.scale(sx, sy);

        //g2d.setTransform(t);
        g2d.setColor(Color.GRAY);

        //g2d.fillRect(100, 100, 10, -10);

        Color histColor = new Color(64, 64, 64);

        for (int i = 0; i < values.length; i++) {
            if (i == maxIndex)
                g2d.setColor(Color.RED.darker());
            else g2d.setColor(histColor);

            int h = (int) (values[i][1] * -sy);
            g2d.fillRect((int) (i * sx + margin), ty - h, (int) Math.ceil(sx), h);
        }

        g2d.setColor(Color.getHSBColor(0, 0.5f, 1));
        //g2d.setTransform(tCopy);
        int NLabels = 20;
        int indexJump = Math.max(values.length / NLabels, 1);
        int y = 5;
        for (int i = 0; i < values.length; i += indexJump) {
            final int x = (int) ((i + 0.25) * sx + margin);
            g2d.drawString(String.format("%.3g", values[i][0]), x, size.height - y);
            final int lineX = (int) (i * sx + margin);
            g2d.drawLine(lineX, size.height, lineX, size.height - margin);
        }


        final double maxX = values[maxIndex][0];
        g2d.drawString(String.format("x=%.3g", maxX), (int) (maxIndex * sx + margin) + 10, size.height / 2);

    }

    private double maxY() {
        double max = 0;
        for (int i = 0; i < values.length; i++) {
            final double v = values[i][1];
            if (v > max) {
                max = v;
                maxIndex = i;
            }
        }

        return max;
    }


}