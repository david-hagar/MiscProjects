package com.david_hagar.gen;


import com.david_hagar.merger.string_counter.StringCount;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandom<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public WeightedRandom() {
        this(new Random());
    }

    public WeightedRandom(Random random) {
        this.random = random;
    }

    public WeightedRandom<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

//    public void remove(double weight, E value){
//        map.remove(weight, value);
//        total-=weight;
//    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }


    public static void main(String[] args) {

        WeightedRandom<String> wr = new WeightedRandom<String>();
        wr.add(1, "C");
        wr.add(100, "A");
        wr.add(10, "B");

        HashMap<String, StringCount> m = new HashMap<>();
        m.put("A", new StringCount("A", 0));
        m.put("B", new StringCount("B", 0));
        m.put("C", new StringCount("C", 0));

        for (int i = 0; i < 101100; i++) {
            m.get(wr.next()).count++;
        }

        System.out.println(m.values());

    }


}