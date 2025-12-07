package com.david_hagar.bui;

import com.david_hagar.util.TarReader;
import com.david_hagar.util.IndexStorage;
import com.david_hagar.util.RateStats;
import com.david_hagar.util.UpdatablePriorityQueue;
import gnu.trove.set.hash.TIntHashSet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BottomUpWithIndex {

    // todo
    //
    // min instance count recycling
    // repeat character missing pair error
    // parser
    // scale up to more text for better stats sample
    // collapse gaps when threshold is reached
    //
    // troubleshoot weird parsings
    // check symbol integrity

    // Similar algorithm: https://en.wikipedia.org/wiki/Re-Pair


    private static final File vocabFile = new File("/Users/hagar/data/working/vocab.txt");
    private static final File symbolInstancesFile = new File("/Users/hagar/data/working/symbolInstances.txt");
    private static final File rawTextFile = new File("/Users/hagar/data/working/rawText.txt");
    private static final int timeToCheckpointVocabSaveInSec = 30;
    private static final int BUFFER_SIZE = Integer.MAX_VALUE -100;
    private final int vocabSize;
    private final int minPairCount;
    private final TarReader tarReader;

    private HashMap<String, Symbol> stringToSymbol;
    private Symbol[] symbolInstances;
    private int nextBlankSymbolIndex; // index after the last symbol instance
    private byte[] nextOffset;
    private byte[] prevOffset;
    private UpdatablePriorityQueue<IdPair> pairMaxHeap;
    private HashMap<IdPair, IdPair> idPairLookup;
    private IdPair tmpPair = new IdPair(null, null);
    private IdPair p;
    private int maxDiff = 0;
    private static final Symbol edgeSymbol = new Symbol("<EDGE SYMBOL>", 2);
    private final TIntHashSet toSkip = new TIntHashSet();
    private final HashSet<IdPair> autoAddPairs = new HashSet<>();


    public BottomUpWithIndex( int vocabSize, int minPairCount, File tarFile) throws IOException {
        this.vocabSize = vocabSize;
        this.minPairCount = minPairCount;
        this.tarReader = new TarReader(tarFile);
    }


    private static class IdPair implements IndexStorage {
        private Symbol s1;
        private Symbol s2;
        private final TIntHashSet locations = new TIntHashSet(10);
        private int heapIndex = -1;

        public IdPair(Symbol s1, Symbol s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof IdPair)) return false;
            IdPair idPair = (IdPair) o;
            return s1.equals(idPair.s1) && s2.equals(idPair.s2);
        }

        @Override
        public int hashCode() {
            return 31 * s1.hashCode() + s2.hashCode();
        }

        @Override
        public String toString() {
            return "IdPair{" +
                    "s1=" + s1 +
                    ", s2=" + s2 +
                    ", locSize=" + locations.size() +
                    '}';
        }

        @Override
        public int getIndex() {
            return heapIndex;
        }

        @Override
        public void setIndex(int index) {
            heapIndex = index;
        }

        public String getConcatValue(){
            return s1.value + s2.value;
        }
    }


    public static HashMap<String, Symbol> initChars(String text) {

        HashMap<String, Symbol> m = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            String c = Character.toString(text.charAt(i));
            Symbol sym = m.get(c);
            if (sym == null) {
                sym = new Symbol(c, 0);
                m.put(c, sym);
            }
            sym.count++;
        }
        return m;
    }

    private void initBuffers() {

        symbolInstances = new Symbol[BUFFER_SIZE];
        nextOffset = new byte[BUFFER_SIZE];
        prevOffset = new byte[BUFFER_SIZE];
        Arrays.fill(nextOffset, (byte) 1);
        Arrays.fill(prevOffset, (byte) -1);
        nextOffset[BUFFER_SIZE - 1] = 0; // end never used.
        prevOffset[0] = 0; // start never used.

        symbolInstances[0] = edgeSymbol;
        symbolInstances[BUFFER_SIZE - 1] = edgeSymbol;


        nextBlankSymbolIndex = 0;




//        for (int i = 0; i < text.length(); i++) {
//            String c = Character.toString(text.charAt(i));
//            symbolInstances[i + 1] = stringToSymbol.get(c);
//        }
    }


    private IdPair getPair(Symbol s1, Symbol s2, boolean addToHeap, boolean requireExist) {
        tmpPair.s1 = s1;
        tmpPair.s2 = s2;

        IdPair p = idPairLookup.get(tmpPair);
        if (p == null) {
            p = tmpPair;
            if (!p.locations.isEmpty())
                throw new RuntimeException("non-empty pair");
            if( requireExist )
                throw new RuntimeException("could not find pair " + p);

            tmpPair = new IdPair(null, null);

            if( addToHeap ) {
                pairMaxHeap.add(p);
                idPairLookup.put(p,p);
            }
        }

        return p;
    }


    private void initPairs() {

        pairMaxHeap = new UpdatablePriorityQueue<>((o1, o2) -> Integer.compare(o2.locations.size(), o1.locations.size()));

        idPairLookup = new HashMap<>();

        for (int i = 0; i < nextBlankSymbolIndex - 1; i++) {
            IdPair p = getPair(symbolInstances[i], symbolInstances[i + 1], false, false);
            p.locations.add(i);
            idPairLookup.put(p,p);
        }

        pairMaxHeap.addAll(idPairLookup.values());
    }


    private Symbol getSymbol(String value){
        Symbol s = stringToSymbol.get(value);
        if( s == null){
            s = new Symbol(value, 0);
            stringToSymbol.put(s.value, s);
        }
        return s;
    }

    private void removePairInstance(int pairIndex1, int pairIndex2) {
        final IdPair pair = getPair(symbolInstances[pairIndex1], symbolInstances[pairIndex2], false, false); // todo: debug errors with dup symbol pairs not being found and requireExist=true
        if(!pair.locations.remove(pairIndex1))
            System.out.println("no instance to remove for index " + pairIndex1 + " on pair " + pair);
        if(pair.locations.isEmpty())
            pairMaxHeap.remove(pair);
        else
            pairMaxHeap.update(pair);
    }

    private void addPairInstance(int pairIndex1, int pairIndex2) {
        final IdPair pair = getPair(symbolInstances[pairIndex1], symbolInstances[pairIndex2], true, false);
        pair.locations.add(pairIndex1);
        pairMaxHeap.update(pair);

        if(stringToSymbol.containsKey(pair.getConcatValue()))
            autoAddPairs.add(pair);
    }



    private void addPair(IdPair p) {

        Symbol s = getSymbol(p.getConcatValue());

        boolean identicalPair = p.s1 == p.s2;

        int[] loc = p.locations.toArray(); // prevents concurrent modification.
        for (int pairIndex1 : loc) {

            if (identicalPair && toSkip.contains(pairIndex1)) {
                toSkip.remove(pairIndex1);
                continue;
            }

            int pairIndex2 = pairIndex1 + nextOffset[pairIndex1];
            int pairIndex0 = pairIndex1 + prevOffset[pairIndex1];
            int pairIndex3 = pairIndex2 + nextOffset[pairIndex2];

            if (identicalPair) {
                if (symbolInstances[pairIndex3] == p.s2)
                    toSkip.add(pairIndex2);
                if (symbolInstances[pairIndex0] == p.s1)
                    toSkip.add(pairIndex0);
            }

            if (symbolInstances[pairIndex1] == p.s1 && symbolInstances[pairIndex2] == p.s2) {
                symbolInstances[pairIndex1].count--;  // todo: remove zero count symbols
                symbolInstances[pairIndex2].count--;

                removePairInstance(pairIndex0, pairIndex1); // remove old overlap pairs
                removePairInstance(pairIndex2, pairIndex3);

                symbolInstances[pairIndex1] = s;    // set symbols
                symbolInstances[pairIndex2] = null;

                prevOffset[pairIndex2] = 0; // null offsets for removed
                nextOffset[pairIndex2] = 0;

                prevOffset[pairIndex3] = checkDiff((pairIndex1 - pairIndex3)); // update offsets
                nextOffset[pairIndex1] = checkDiff((pairIndex3 - pairIndex1));

                addPairInstance(pairIndex0, pairIndex1); // add new overlap pairs
                addPairInstance(pairIndex1, pairIndex3);

                s.count++;
            } else {
                System.out.println("symbol mismatch for pair instance at index " + pairIndex1 + " p=" + p);
                System.out.println("expected: s1: " + p.s1 + " s2: " + p.s2);
                System.out.println("actual: s1: " + symbolInstances[pairIndex1] + " s2: " + symbolInstances[pairIndex2]);
            }
        }
        if(identicalPair)
            toSkip.clear();

    }


    private byte checkDiff(int diff) {
        int absDiff = Math.abs(diff);
        if (absDiff > maxDiff)
            maxDiff = absDiff;

        if (diff < Byte.MIN_VALUE || diff > Byte.MAX_VALUE)
            throw new RuntimeException("byte offset exceeded.");

        return (byte) diff;
    }


    private void saveVocab() throws IOException {
        System.out.println("Saving vocab.");
        ArrayList<Symbol> toSort = new ArrayList<>(stringToSymbol.values());
        toSort.sort((o1, o2) -> Integer.compare(o2.count, o1.count));

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BottomUpWithIndex.vocabFile), StandardCharsets.UTF_8))) {
            for (Symbol symbol : toSort) {
                bw.write('\'' + symbol.value + "' " + symbol.count);
                bw.newLine();
            }
        }
    }


    private void saveText() throws IOException {
        System.out.println("saving text ...");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BottomUpWithIndex.symbolInstancesFile), StandardCharsets.UTF_8))) {
            for (int i = 1; symbolInstances[i] != edgeSymbol; i += nextOffset[i]) {
                bw.write(symbolInstances[i].value);
                bw.write('_');

                if (i % 80 == 79)
                    bw.newLine();
            }
        }
        System.out.println("save text done.");
    }



    private void fillBuffer() {

        while( nextBlankSymbolIndex < BUFFER_SIZE ){

        }

    }


    private class StatusPrinter {
        @Override
        public String toString() {
            return "lastInstanceIndex = " + nextBlankSymbolIndex + " VocabCount = " + stringToSymbol.size() + " pairCount = " + idPairLookup.size() + " p = " + p;
        }
    }


    private void buildVocabulary() throws IOException {
//        stringToSymbol = initChars(text);
        initBuffers();
//        initPairs();
        fillBuffer();



        RateStats rs = new RateStats(1000, "pair");
        long lastCheckPoint = System.currentTimeMillis();
        StatusPrinter sp = new StatusPrinter();


        while ((p = pairMaxHeap.poll()) != null) {
            idPairLookup.remove(p);

            // System.out.println("p=" + p);
            if (p.locations.size() <= minPairCount)
            {
                System.out.println("*** minPairCount reached");
                break;
            }

            addPair(p);
            for (IdPair pair : autoAddPairs) {
                addPair(pair);
            }
            autoAddPairs.clear();


            if (stringToSymbol.size() >= vocabSize) {
                System.out.println("*** max vocab reached.");
                break;
            }

            if (System.currentTimeMillis() - lastCheckPoint > timeToCheckpointVocabSaveInSec *1000) {
                saveVocab();
                saveText();
                lastCheckPoint = System.currentTimeMillis();
            }
            rs.oneEntryProcessed(1, sp);
        }

        if(pairMaxHeap.isEmpty())
            System.out.println("*** empty pair heap.");

        System.out.println("Saving ...");
        saveVocab();
        saveText();
        //FileUtils.writeStringToFile(rawTextFile, text, StandardCharsets.UTF_8);

        rs.printEndStats();

        System.out.println("Done.");
    }


    public static void main(String[] args) {
        // File inputFile = new File("/Users/hagar/data/generated.tar.gz");
        File inputFile = new File("/Users/hagar/data/MovieSummaries/plot_summaries_small.tar.gz");

        try {
//            String text = getTextFromTar(inputFile, (Integer.MAX_VALUE-500)/2);   //
            BottomUpWithIndex t = new BottomUpWithIndex( 10000, 3, inputFile);
            t.buildVocabulary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
