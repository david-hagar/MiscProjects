package com.davidhagar.gridphysics.util;


import java.util.ArrayList;
import java.util.Random;

public class RandomSelector<T> {
    private final ArrayList<T> list = new ArrayList<>();
    private final Random random;

    public RandomSelector(long seed) {
        random = new Random(seed);
    }

    public void add(T obj){
         list.add(obj);
    }

    public T selectRandomObject() {
       return list.get((int) (random.nextFloat() * list.size()));
    }


}
