package com.davidhagar.gridphysics.ui;

import com.davidhagar.gridphysics.Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridWindow {
    private final Sim sim;
    private JPanel panel1;
    private JPanel controls;
    private JPanel renderPanel;
    private JButton stopButton;
    private JButton startButton;


    public GridWindow(Sim sim) {
        this.sim = sim;
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.stop();
            }
        });

        sim.setLoopListener(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        renderPanel.repaint();
                    }
                });
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.start();
            }
        });

        controls.add(sim.stateFunction.getControls());
    }

    private void createUIComponents() {
        renderPanel = new RenderPanel(sim);
    }


    private static void centerFrame(JFrame frame, int margin) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= margin * 2;
        screenSize.width -= margin * 2;
        frame.setSize(screenSize);
        frame.setLocation(margin, margin);
    }

    public void openWindow() {

        JFrame frame = new JFrame("GridWindow");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setBackground(Color.BLACK);
        centerFrame(frame, 50);
        frame.setVisible(true);


    }
}
