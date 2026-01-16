package com.davidhagar.gridphysics.functions.exp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RatePanel extends JPanel {

    JTextArea expressionLabel = new JTextArea();

    public RatePanel(ExpressionFunction expressionFunction) {

        this.setLayout(new FlowLayout());
        int maxRate = 7;
        for (int i = 1; i <= maxRate; i++) {

            JButton rateButton = new JButton("" + i);
            int finalI = i;
            rateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    expressionFunction.rate(finalI);
                }
            });

            rateButton.setPreferredSize(new Dimension(30,30));

            this.add(rateButton);

            expressionLabel.setForeground(Color.white);
            expressionLabel.setBackground(Color.BLACK);
        }

        JButton savebutton = new JButton("Save");

        savebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expressionFunction.save();
            }
        });

        this.add(savebutton);

        this.add(expressionLabel);

    }

    public void setExpression(String expressionString){
        expressionLabel.setText(expressionString);
    }
}
