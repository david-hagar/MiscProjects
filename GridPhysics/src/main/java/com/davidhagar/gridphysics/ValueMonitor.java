package com.davidhagar.gridphysics;

import java.util.ArrayDeque;

public class ValueMonitor {

    private static interface Check {
        public boolean check(double prevValue, double value);
    }


        private static class Flags{
        public boolean increasing;
        public boolean decreasing;
        public boolean equal;
        public double value;

        public Flags(boolean increasing, boolean decreasing, boolean equal, double value) {
            this.increasing = increasing;
            this.decreasing = decreasing;
            this.equal = equal;
            this.value = value;
        }
    }

    ArrayDeque<Flags> queue;

    int incCount = 0;
    int decCount = 0;
    int equalCount = 0;

    public ValueMonitor(int history) {
         queue = new ArrayDeque<>(history);
    }


    public void add(double value){
        Flags last = queue.getLast();
        Flags next
    }
}
