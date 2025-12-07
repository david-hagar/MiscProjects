package com.davidhagar.prob;

import com.davidhagar.prob.sample_listener.SampleListener;
import com.davidhagar.prob.variable.RandomVariable;

import java.util.ArrayList;

public class Sampler {


    RandomVariable randomVariable;
    ArrayList<SampleListener> listeners = new ArrayList<>();

    public Sampler(RandomVariable randomVariable) {
        this.randomVariable = randomVariable;
    }

    public void sample(int n){

        for (int i = 0; i < n; i++) {
            double v = randomVariable.next();

            for (SampleListener listener : listeners) {
                listener.next(v);
            }
        }
    }

    public void addListener( SampleListener listener){
        listeners.add(listener);
    }
}
