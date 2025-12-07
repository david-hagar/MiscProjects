package com.davidhagar.coprime;


import java.util.ArrayList;

public class Coprimes {


    static private int gcd(int a, int b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }


    static public void generate(int n, CoprimeListener listener) {

        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= i; j++)
                if (gcd(i, j) == 1)
                    listener.processCoprime(j, i);
    }

    static public void generateSeq(int max, CoprimeListener listener) {

        ArrayList<CoprimeSet> last = new ArrayList<>(3);
        add(listener, last, max, new CoprimeSet(1, 1));
        last.clear();

        add(listener, last, max, new CoprimeSet(2, 1));
        add(listener, last, max, new CoprimeSet(3, 1));

        while (true) {
            ArrayList<CoprimeSet> nextList = new ArrayList<>(last.size() * 3);
            if (last.isEmpty())
                break;
            for (CoprimeSet s : last) {
                add(listener, nextList, max, new CoprimeSet(2 * s.a - s.b, s.a));
                add(listener, nextList, max, new CoprimeSet(2 * s.a + s.b, s.a));
                add(listener, nextList, max, new CoprimeSet(s.a + 2 * s.b, s.b));
            }
            last = nextList;
        }
    }

    private static void add(CoprimeListener listener, ArrayList<CoprimeSet> nextList, int max, CoprimeSet s) {
        if (s.a <= max && s.b <= max) {
            if (s.a < s.b)
                listener.processCoprime(s.a, s.b);
            else
                listener.processCoprime(s.b, s.a);
            nextList.add(s);
        }
    }


}
