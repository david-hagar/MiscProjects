package com.david_hagar.token_scanner;

import com.david_hagar.merger.string_counter.StringCount;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class SelectionFilter {

    private final SelectionVisitor selectionVisitor;

    public interface SelectionVisitor {
        public void visit(StringCount sc) throws IOException;
    }


    PriorityQueue<StringCount> countSort = new PriorityQueue<>(new Comparator<StringCount>() {
        @Override
        public int compare(StringCount o1, StringCount o2) {
            return -Long.compare(o1.count, o2.count);
        }
    });

    HashMap<String, StringCount> keyLookup = new HashMap<>();


    public SelectionFilter(SelectionVisitor selectionVisitor) {
        this.selectionVisitor = selectionVisitor;
    }


    private void add(StringCount sc) {
        if (keyLookup.containsKey(sc.getKey())) {
            StringCount prev = keyLookup.remove(sc.getKey());

            if (prev.count > 0 && sc.count >0)
                System.out.println("warn: updating two positive counts on: " + sc + " prev:" + prev);

            countSort.remove(prev);
            prev.count += sc.count;
            keyLookup.put(prev.getKey(), prev);
            countSort.add(prev);
        }else{
            keyLookup.put(sc.getKey(), sc);
            countSort.add(sc);
        }


    }


    private void subtractSubNGrams(StringCount sc) {
        if( sc.count < 0)
            System.out.println("warn: neg subtract");

        int count = sc.count / sc.value.length();
        try {
            NgramGenerator.generate(sc.value, new NgramGenerator.NGramVisitor() {
                @Override
                public void visit(String ngram) throws IOException {
                    add(new StringCount(ngram, -ngram.length() * count));
                }
            }, Config.MIN_NGRAM_LENGTH, Math.min(sc.value.length()-1, Config.MAX_NGRAM_LENGTH));
        } catch (IOException e) {
            e.printStackTrace(); // ignore because will never happen
        }
    }

    public void putNext(StringCount sc) throws IOException {
        //selectionVisitor.visit(sc);

        while(!countSort.isEmpty() && countSort.peek().count >= sc.count){
            StringCount sc2 = countSort.poll();
            keyLookup.remove(sc2.getKey());
            subtractSubNGrams(sc2);
            selectionVisitor.visit(sc2);
        }

        if( keyLookup.containsKey(sc.getKey()))
            add(sc);
        else{
            subtractSubNGrams(sc);
            selectionVisitor.visit(sc);
        }
    }

}




