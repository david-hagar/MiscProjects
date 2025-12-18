package com.davidhagar.gridphysics.genetic;

import java.util.ArrayList;

public interface Population {

    public GeneticObject select();
    public GeneticObject add(GeneticObject geneticObject);
    public int getMaxSize();
    }
