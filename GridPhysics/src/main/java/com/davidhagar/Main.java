package com.davidhagar;

import com.davidhagar.gridphysics.Sim;
import com.davidhagar.gridphysics.ui.GridWindow;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UIManager.put("Panel.background", Color.BLACK);

        Sim sim = Sim.makeWave();
//        Sim sim = Sim.makeSimple();
        GridWindow w = new GridWindow(sim);
        w.openWindow();
        sim.start();
    }
}