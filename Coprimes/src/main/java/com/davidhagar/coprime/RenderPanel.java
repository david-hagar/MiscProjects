package com.davidhagar.coprime;


import com.davidhagar.coprime.options.OptionStartup;
import com.davidhagar.coprime.options.RenderOption;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 */
public class RenderPanel extends JPanel {

    Point2D.Float tmpPoint = new Point2D.Float();
    Rectangle2D.Float tmpRect = new Rectangle2D.Float();
    Ellipse2D.Float tmpEllipse = new Ellipse2D.Float();
    private RenderOption renderOption;

    public RenderPanel() {
        //this.setDoubleBuffered(false);
        //RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

        renderOption = new OptionStartup();
    }


    private void drawPoint(Graphics2D g, float x, float y, float pointHalfWidth) {

        tmpRect.x = x - pointHalfWidth;
        tmpRect.y = y - pointHalfWidth;
        tmpRect.width = pointHalfWidth * 2;
        tmpRect.height = pointHalfWidth * 2;

        g.fill(tmpRect);
    }

    private void drawCircle(Graphics2D g, float x, float y, float pointHalfWidth) {

        tmpEllipse.x = x - pointHalfWidth;
        tmpEllipse.y = y - pointHalfWidth;
        tmpEllipse.width = pointHalfWidth * 2;
        tmpEllipse.height = pointHalfWidth * 2;

        g.fill(tmpEllipse);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void drawCircle4(Graphics2D g, float x, float y, float pointWidth) {
        drawCircle(g, x, y, pointWidth);
        drawCircle(g, y, x, pointWidth);
        drawCircle(g, -x, y, pointWidth);
        drawCircle(g, -y, x, pointWidth);
        drawCircle(g, x, -y, pointWidth);
        drawCircle(g, y, -x, pointWidth);
        drawCircle(g, -x, -y, pointWidth);
        drawCircle(g, -y, -x, pointWidth);
    }

    private float calcPointWidth(float z) {
        Point2D.Float p = new Point2D.Float();
//        project(renderOption.dotSize, 0, z, p);
        return p.x;
    }

    private Point2D.Double findViewEdge(float depth, double screenX, double screenY) {
        float k = (renderOption.viewToScreen + depth) / renderOption.viewToScreen;
        return new Point2D.Double(screenX / 2 * k, screenY / 2 * k);
    }


    private Point2D.Float circularizeOld(float x, float y, float boxMax) {

        double yp = y / x * boxMax;
        double d = Math.sqrt(boxMax * boxMax + yp * yp);

        double k = d / (Math.sqrt(x * x + y * y) );
        this.tmpPoint.x = (float) (x * k);
        this.tmpPoint.y = (float) (y * k);

        return this.tmpPoint;
    }

    private Point2D.Float circularize(float x, float y, float boxMax) {

        double ratio = y/x;
        double k = 1/Math.sqrt(1 + ratio * ratio);

        this.tmpPoint.x = (float) (x * k);
        this.tmpPoint.y = (float) (y * k);

        return this.tmpPoint;
    }


    private void draw(Graphics2D g, double screenX, double screenY) {
        g.setColor(Color.black);

        int max = 150;
        Coprimes.generateSeq(max, (a, b) -> {
//            float mag = (float) Math.sqrt(a*a +b*b);
            //float k = max / b;
            //float mag = (float) Math.sqrt(b * b + b * b);


            float x = a / (float) max;
            float y = b / (float) max;
            drawCircle4(g, x, y, 1.0f/max/2);

//            Point2D.Float p = circularize(b/(float)max, a/(float)max, 1);
//            drawCircle4(g, p.x, p.y, 1.0f/max/2);
        });
    }


    private void drawCoordinateTest(Graphics2D g, double screenX, double screenY) {

        g.setColor(Color.getHSBColor(0, 1.0f, 1.0f));
        drawPoint(g, 0, 0, 0.01f);

        g.setColor(Color.getHSBColor(0.33f, 1.0f, 1.0f));
        drawPoint(g, -1, 0, 0.01f);

        g.setColor(Color.getHSBColor(0.66f, 1.0f, 1.0f));
        drawPoint(g, 0, -1, 0.01f);
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;


        Dimension d = this.getSize();


        drawAll(g, 0, 0, d.getWidth(), d.getHeight());
    }

    public void setRenderOption(RenderOption renderOption) {
        this.renderOption = renderOption;
        this.repaint();
    }


    public void drawAll(Graphics2D g, double x, double y, double width, double height) {

        RenderingHints renderHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(renderHints);

        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        //g.setColor(Color.BLACK);
        //g.drawLine(0,0,d.width,d.height);

        float virtualScreenHeight = 1;
        g.translate(x + width / 2, y + height / 2);
        double k = height / 2 / virtualScreenHeight;
        g.scale(k * 0.95, -k * 0.95);

//        drawCoordinateTest(g, width / k, height / k);
        draw(g, width / k, height / k);
    }
}
