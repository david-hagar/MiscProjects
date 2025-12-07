package com.davidhagar.coprime;

public class CoprimeSet implements Comparable<CoprimeSet> {
    int a;
    int b;

    public CoprimeSet(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CoprimeSet) {
            CoprimeSet coprimeSet = (CoprimeSet) obj;
            return a == coprimeSet.a && b == coprimeSet.b;
        } else return false;
    }

    @Override
    public int compareTo(CoprimeSet o) {
        int c = Integer.compare(this.a, o.a);
        if (c != 0)
            return c;
        return Integer.compare(this.b, o.b);
    }

    @Override
    public String toString() {
        return "[" + this.a + ", " + this.b + "]";
    }
}
