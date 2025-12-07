package com.david_hagar.merger.string_counter;

import com.david_hagar.merger.Mergable;

public class StringCount implements Mergable {

    public final String value;
    public int count;

    public StringCount(String value, int count) {
        this.value = value;
        this.count = count;
    }

    @Override
    public String getKey() {
        return value;
    }

    @Override
    public void merge(Mergable mergable) {
        this.count += ((StringCount)mergable).count;
    }


    @Override
    public String toString() {
        return "StringCount{" +
                "value='" + value.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n") + '\'' +
                ", count=" + count +
                '}';
    }
}
