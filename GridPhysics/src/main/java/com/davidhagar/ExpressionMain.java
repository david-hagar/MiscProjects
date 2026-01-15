package com.davidhagar;

import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.ui.GridWindow;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ExpressionMain {
    public static void main(String[] args) {
        UIManager.put("Panel.background", Color.BLACK);

        Sim sim = Sim.makeExpression();
        GridWindow w = new GridWindow(sim);
        w.openWindow();
        sim.start();
    }
}