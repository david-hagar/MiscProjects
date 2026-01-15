package com.davidhagar.gridphysics.ui;

import com.davidhagar.gridphysics.GridContainer;
import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RenderPanel extends JPanel {


    private final GridContainer gridContainer;
    private final Sim sim;
    private BufferedImage image;
    private int loopCountCopy = 0;
    private GridStats gridStats;

    public RenderPanel(Sim sim) {
        this.gridContainer = sim.gridContainer;
        this.sim = sim;

        int[] size = gridContainer.getSize();
        image = new BufferedImage(size[0], size[1],BufferedImage.TYPE_INT_RGB);

    }


    @Override
    protected void paintComponent(Graphics g) {
        updateImage();
        //super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Calculate scaling factors
            double panelWidth = getWidth();
            double panelHeight = getHeight();
            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();

            double scaleX = panelWidth / imageWidth;
            double scaleY = panelHeight / imageHeight;

            // Use the smaller scale factor to maintain aspect ratio
            double scale = Math.min(scaleX, scaleY);

            // Calculate the new dimensions of the scaled image
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);

            // Calculate the position to center the image
            int x = (int) ((panelWidth - scaledWidth) / 2);
            int y = (int) ((panelHeight - scaledHeight) / 2);

            // Draw the scaled image
            g2d.drawImage(image, x, y, scaledWidth, scaledHeight, this);
            g2d.setColor(Color.RED.darker());
            g2d.drawString( Integer.toString( loopCountCopy), 10,100);
            g2d.drawString( "min = " + Arrays.toString( gridStats.min ), 10,130);
            g2d.drawString( "max = " + Arrays.toString( gridStats.max ), 10,160);
        }


    }

    private void updateImage() {
        synchronized (sim) {
            gridStats = sim.getStats();
            int length = gridContainer.grid.length;
            for (int i = 0; i < length; i++) {
                int length2 = gridContainer.grid[0].length;
                for (int j = 0; j < length2; j++) {
                    image.setRGB(i, j, sim.stateFunction.getRGB(gridContainer.grid[i][j], gridStats));
                    //grid.grid[i][j]
                }
            }
            loopCountCopy = sim.getLoopCount();

        }

    }
}
