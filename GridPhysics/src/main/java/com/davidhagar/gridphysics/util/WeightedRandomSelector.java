package com.davidhagar.gridphysics.util;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandomSelector<T> {
    private final TreeMap<Double, T> map = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0.0;

    public WeightedRandomSelector() {
    }

    public void add(T obj, double weight){
        totalWeight += weight;
        map.put(totalWeight, obj);
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public T selectRandomObject() {
        // Generate a random value between 0 and the total weight
        double value = random.nextDouble() * totalWeight;
        // Get the entry whose key is greater than or equal to the random value
        return map.ceilingEntry(value).getValue();
    }


}
