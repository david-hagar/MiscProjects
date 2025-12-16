package com.davidhagar.gridphysics.genetic;

import java.util.ArrayList;

public interface Population {

    public GeneticObject select();
    public void add(GeneticObject geneticObject);

}
