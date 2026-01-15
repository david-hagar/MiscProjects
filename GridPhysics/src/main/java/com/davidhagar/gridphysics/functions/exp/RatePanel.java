package com.davidhagar.gridphysics.functions.exp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RatePanel extends JPanel {
    private final ExpressionFunction expressionFunction;


    public RatePanel(ExpressionFunction expressionFunction) {
        this.expressionFunction = expressionFunction;

        int maxRate = 5;
        for (int i = 1; i <= maxRate; i++) {

            JButton ratebutton = new JButton("" + i);
            int finalI = i;
            ratebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    expressionFunction.rate(finalI);
                }
            });
        }

        JButton savebutton = new JButton("Save");

        savebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expressionFunction.save();
            }
        });
    }


}
