package com.david_hagar.gen;

import com.david_hagar.merger.Mergable;

public class StringGoalCount {

    public final String value;
    public int count;
    public int goalCount;

    public StringGoalCount(String value, int count, int goalCount) {
        this.value = value;
        this.count = count;
        this.goalCount = goalCount;
    }


    @Override
    public String toString() {
        return "StringGoalCount{" +
                "value='" + value + '\'' +
                ", count=" + count +
                ", goalCount=" + goalCount +
                '}';
    }
}

