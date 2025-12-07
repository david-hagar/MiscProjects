package com.davidhagar.prob;

import com.davidhagar.prob.variable.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private static final int sampleN = 100 * 1000;
    private static final int sampleNLarge = 1000 * 1000;
    private static final int sampleNVLarge = 10 * 1000 * 1000;

    private static DisplayWindow dw;

    public static class ListElement {

        private final RandomVariable rv;
        private final int sampleN;
        private final int binCount;

        public ListElement(RandomVariable rv, int sampleN, int binCount) {
            this.rv = rv;
            this.sampleN = sampleN;
            this.binCount = binCount;
        }

        @Override
        public String toString() {
            return rv.toString();
        }
    }

    private static void addJList(JFrame frame) {
        ListElement[] elements = {
                new ListElement(new Binomial(100, 0.5), sampleN, 200),
                new ListElement(new Poisson(200), sampleNLarge, 800),
                new ListElement(new Square(new Uniform(0, 1)), sampleN, 400),
                new ListElement(new Square(new Uniform(-1, 1)), sampleN, 400),
                new ListElement(new NSum(new Square(new Uniform(0, 1)), 2), sampleNLarge, 200),
                new ListElement(new NSum(new Square(new Uniform(0, 1)), 3), sampleNLarge, 200),
                new ListElement(new NSum(new Square(new Uniform(0, 1)), 4), sampleNLarge, 200),
                new ListElement(new NSum(new Square(new Uniform(0, 1)), 8), sampleNLarge, 200),
                new ListElement(new NSum(new Square(new Uniform(0, 1)), 32), sampleNLarge, 200),
                new ListElement(new Square(new Square(new Binomial(100, 0.5))), sampleN, 400),
                new ListElement(new NSum(new Uniform(0, 1), 2), sampleNVLarge, 400),
                new ListElement(new NSum(new Uniform(0, 1), 200), 4000000, 400),
                new ListElement(new NProduct(new Uniform(1, 2), 8), sampleNLarge, 400),
                new ListElement(new Uniform(10, 20), sampleNVLarge, 400)};

        JList<ListElement> list = getListElementJList(elements);

        frame.add(list, BorderLayout.WEST);
    }

    private static JList<ListElement> getListElementJList(ListElement[] elements) {
        JList<ListElement> list = new JList<>(elements);
        list.setForeground(Color.WHITE);
        list.setBackground(Color.BLACK);

        list.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            ListElement element = list.getSelectedValue();
            System.out.println(element);
            dw.setValues(null);
            SwingUtilities.invokeLater(() -> {
                double[][] values = Main.getDistribution(element.rv, element.sampleN, element.binCount);
                dw.setValues(values);
            });

        });
        return list;
    }


    public static JFrame openFrame(double[][] values) {
        JFrame frame = new JFrame("Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        dw = new DisplayWindow(values);
        frame.add(dw, BorderLayout.CENTER);
        addJList(frame);
        centerFrame(frame);

        return frame;
    }

    private static void centerFrame(JFrame frame) {
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
