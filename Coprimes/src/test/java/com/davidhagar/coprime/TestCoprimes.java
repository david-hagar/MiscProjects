package com.davidhagar.coprime;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class TestCoprimes {




    @Test
    public void test() {

        ArrayList<CoprimeSet> list1 = new ArrayList<>();
         long start1 = System.currentTimeMillis();
        int max = 8;
        Coprimes.generate(max, (a, b) -> {
            //System.out.println( a + " " + b);
            list1.add(new CoprimeSet(a,b));
        });

         System.out.println( );

        ArrayList<CoprimeSet> list2 = new ArrayList<>();
        long start2 = System.currentTimeMillis();
        Coprimes.generateSeq(max, (a, b) -> {
            //System.out.println( a + " " + b);
            list2.add(new CoprimeSet(a,b));
        });

        System.out.println( "time2 = " + (System.currentTimeMillis() - start2) + " ms" );
        System.out.println( "time1 = " + (System.currentTimeMillis() - start1) + " ms" );
        Collections.sort(list1);
        Collections.sort(list2);
        printList(list1);
        printList(list2);
        System.out.println( "size1 = " + list1.size() + " size2 = " + list2.size());
        System.out.println( "size matches = " + (list1.size() == list2.size()) );
        System.out.println( "equals = " + list1.equals(list2) );
    }

    private void printList(ArrayList<CoprimeSet> list) {
        for (CoprimeSet s: list)
            System.out.print(s);
        System.out.println();
    }


}
