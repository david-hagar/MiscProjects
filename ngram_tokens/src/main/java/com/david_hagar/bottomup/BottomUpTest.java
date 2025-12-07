package com.david_hagar.bottomup;

import com.david_hagar.util.TarProcessor;
import com.david_hagar.util.RateStats;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BottomUpTest {

    // issues
    // what about repeating identical symbols? Overlaps double counted?
    // exact dups?
    // if a symbol count is lowered below the current new pair count it can be demoted and broken up so that its pieces do exceed the pair count
    // out of core for larger text?
    // linked list version with updatable heap that is more efficient
    // suboptimal encodings are generated due to origin of sequences. Ex. " s_itting " and "_sitting "
    // '0' -392 (negative)


    private final String text;
    private final int vocabSize;
    private final int minPairCount;
    private final boolean rebuildPairs;

    private HashMap<String, Symbol> stringToSymbol;
    private ArrayList<Symbol> idToSymbol;
    private int[] symbolInstances;
    private int symbolInstancesCount = 0;
    private PriorityQueue<IdPair> pairs;
    private File vocabFile = new File("/Users/hagar/data/working/vocab.txt");
    private File symbolInstancesFile = new File("/Users/hagar/data/working/symbolInstances.txt");
    private File rawTextFile = new File("/Users/hagar/data/working/rawText.txt");

    private IdPair p;

    public BottomUpTest(String text, int vocabSize, int minPairCount, boolean rebuildPairs) {
        this.text = text;
        this.vocabSize = vocabSize;
        this.minPairCount = minPairCount;
        this.rebuildPairs = rebuildPairs;
    }


    private static class IdPair {
        private int id1;
        private int id2;
        private int count;

        public IdPair(int id1, int id2, int count) {

            this.id1 = id1;
            this.id2 = id2;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdPair idPair = (IdPair) o;
            return id1 == idPair.id1 && id2 == idPair.id2;
        }

        @Override
        public int hashCode() {
            //return Objects.hash(id1, id2);
            return 3331 * id1 + 7919*id2;
        }

        @Override
        public String toString() {
            return "IdPair{" +
                    "id1=" + id1 +
                    ", id2=" + id2 +
                    ", count=" + count +
                    '}';
        }
    }

    public String getString(IdPair p) {
        return idToSymbol.get(p.id1).value + idToSymbol.get(p.id2).value;
    }

    public String getStringDebug(IdPair p) {
        return '\'' + idToSymbol.get(p.id1).value + "'-'" + idToSymbol.get(p.id2).value + '\'';
    }


    private void saveVocab(File f) throws IOException {

        ArrayList<Symbol> toSort = new ArrayList<>(idToSymbol);
        Collections.sort(toSort, new Comparator<Symbol>() {
            @Override
            public int compare(Symbol o1, Symbol o2) {
                return Integer.compare(o2.count, o1.count);
            }
        });

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            for (Symbol symbol : toSort) {
                bw.write('\'' + symbol.value + "' " + symbol.count);
                bw.newLine();
            }
        }
    }


    private void saveText(File f) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            for (int i = 0; i < symbolInstancesCount; i++) {
                bw.write(idToSymbol.get(symbolInstances[i]).value);
                bw.write('_');

                if (i % 100 == 0)
                    bw.newLine();
            }

        }
    }


    private void checkSymbolIntegrity() {

        int c = 0;
        for (int i = 0; i < symbolInstancesCount; i++) {
            final Symbol symbol = idToSymbol.get(symbolInstances[i]);
            String s = symbol.value;
            for (int j = 0; j < s.length(); j++) {
                if(text.charAt(c+j) != s.charAt(j)) {
                    final String substring = text.substring(c + j, Math.min(c + j + 40, text.length()));
                    throw new RuntimeException("mismatch at: " + substring + ", sym = " + symbol);
                }
            }
            c+=s.length();
        }
    }


    public static HashMap<String, Symbol> getChars(String text) {

        HashMap<String, Symbol> m = new HashMap<>();
        int idCount = 0;
        for (int i = 0; i < text.length(); i++) {
            String c = Character.toString(text.charAt(i));
            Symbol sym = m.get(c);
            if (sym == null) {
                sym = new Symbol(c, 0, idCount++);
                m.put(sym.value, sym);
            }
            sym.count++;
        }
        return m;
    }


    private static void fillsym(int[] symbols, HashMap<String, Symbol> stringToSymbol, String text) {
        for (int i = 0; i < text.length(); i++) {
            String c = Character.toString(text.charAt(i));
            symbols[i] = stringToSymbol.get(c).id;
        }
    }

    private static double entropy(int count, int totalCount) {
        float p = ((float) count) / totalCount;
        return -p * Math.log(p);
    }

    private static double information(int count, int totalCount) {
        if (count == 0)
            return 0.0;

        return -count * Math.log(((double) count) / totalCount);
    }


    private static PriorityQueue<Symbol> buildBottomCounts(HashMap<String, Symbol> stringToSymbol) {

        PriorityQueue<Symbol> q = new PriorityQueue<Symbol>(stringToSymbol.size(), new Comparator<Symbol>() {
            @Override
            public int compare(Symbol o1, Symbol o2) {
                return Integer.compare(o1.count, o2.count);
            }
        });
        q.addAll(stringToSymbol.values());
        return q;
    }


    private PriorityQueue<IdPair> makePairs() {

        HashMap<IdPair, IdPair> m = new HashMap<>();

        IdPair p = new IdPair(0, 0, 0);
        for (int i = 1; i < symbolInstancesCount; i++) {
            p.id1 = symbolInstances[i - 1];
            p.id2 = symbolInstances[i];
            IdPair other = m.get(p);
            if (other == null) {
                m.put(p, p);
                other = p;
                p = new IdPair(0, 0, 0);
            }

            other.count++;
        }
        //System.out.println("sorting pairs. Size = " + m.size());
        PriorityQueue<IdPair> q = new PriorityQueue<IdPair>(new Comparator<IdPair>() {
            @Override
            public int compare(IdPair o1, IdPair o2) {
                return Integer.compare(o2.count, o1.count);
            }
        });
        q.addAll(m.values());
        return q;
    }

    private boolean isBetter(IdPair p) {

        double e1 = information(idToSymbol.get(p.id1).count, symbolInstancesCount);
        double e2 = information(idToSymbol.get(p.id2).count, symbolInstancesCount);
        double e1_after = information(idToSymbol.get(p.id1).count - p.count, symbolInstancesCount - p.count);
        double e2_after = information(idToSymbol.get(p.id2).count - p.count, symbolInstancesCount - p.count);
        double eCombine = information(p.count, symbolInstancesCount - p.count);

        boolean isBetter = e1 + e2 > e1_after + e2_after + eCombine;
        //System.out.println(getStringDebug(p) + " better = " + isBetter);
        return isBetter;
    }

    private void decrement(int id, int count) {
        Symbol s = idToSymbol.get(id);
        s.count -= count;
        //if (s.count == 0)
            //stringToSymbol.remove(s.value);
    }

    public void addPair(IdPair p) {

        Symbol s = new Symbol(getString(p), p.count, stringToSymbol.size());
        //System.out.println("new symbol = " + s);



        decrement(p.id1, p.count);
        decrement(p.id2, p.count);

        replacePairs(p, s);

        stringToSymbol.put(s.value, s);
        idToSymbol.add(s);
//        if (s.id >= 290)
//            checkSymbolIntegrity();

        if (rebuildPairs)
            pairs = makePairs();

//        if (s.id >= 290)
//            checkSymbolIntegrity();

    }

    private void replacePairs(IdPair p, Symbol s) {
        final int newID = s.id;
        int[] newSymbols = new int[symbolInstancesCount];
        int count = 0;
        for (int i = 0; i < symbolInstancesCount - 1; i++) {
            if (symbolInstances[i] == p.id1 && symbolInstances[i + 1] == p.id2) {
                newSymbols[count++] = newID;
                i++;
            } else
                newSymbols[count++] = symbolInstances[i];
        }

        symbolInstances = newSymbols;
        symbolInstancesCount = count;
    }

    private class StatusPrinter{
        @Override
        public String toString() {
            return "symbolInstances = " + symbolInstancesCount + " VocabCount = " + stringToSymbol.size() + " pairCount = " + pairs.size() + " p = " + p;
        }
    }

    public void buildVocabulary() throws IOException {

        stringToSymbol = getChars(text);
        System.out.println("initial chars = " + stringToSymbol.size());

        idToSymbol = new ArrayList<Symbol>(Arrays.asList(new Symbol[stringToSymbol.size()]));  // init to list of nulls

        for (Symbol v : stringToSymbol.values()) {
            idToSymbol.set(v.id, v);
        }

        symbolInstances = new int[text.length()];
        fillsym(symbolInstances, stringToSymbol, text);
        symbolInstancesCount = text.length();

        RateStats rs = new RateStats(1000, "pair");
        pairs = makePairs();

        int pairCount = 0;

        StatusPrinter sp = new StatusPrinter();

        boolean done = false;
        while (!done) {


            while ((p = pairs.poll()) != null) {


                if (p.count <= minPairCount) {
                    done = true;
                    break;
                }

                //if (isBetter(p))
                //{
                    addPair(p);
                //}

                if (stringToSymbol.size() >= vocabSize) {
                    System.out.println("max vocab reached.");
                    done = true;
                    break;
                }

                if( ++pairCount % 500 == 0){
                    saveVocab(vocabFile);
                    saveText(symbolInstancesFile);
                }
                rs.oneEntryProcessed(1, sp);
            }


        }

        saveVocab(vocabFile);
        saveText(symbolInstancesFile);
        FileUtils.writeStringToFile(rawTextFile, text, StandardCharsets.UTF_8);

        checkSymbolIntegrity();
    }


    public static String getTextFromTar(File tarFile, int nChar) throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder(nChar);

        TarProcessor.unTarFile(tarFile, new TarProcessor.DocVisitor() {
            @Override
            public boolean visit(String doc, TarArchiveEntry tarEntry, RateStats stats) throws IOException {
                if (doc.length() == 0)
                    System.out.println("zero length document");
                // System.out.println(body);
                if( sb.length() + doc.length() < nChar )
                    sb.append(doc.toLowerCase());
                else
                    sb.append(doc, 0, nChar-sb.length() );
                //System.out.println(tarEntry.getName());
                stats.oneEntryProcessed(doc.length(), tarEntry.getName());
                return sb.length() < nChar;
            }
        });

        System.out.println("text length = " + sb.length());

        return sb.toString();
    }


    public static void main(String[] args) {
        // File inputFile = new File("/Users/hagar/data/generated.tar.gz");
        File inputFile = new File("/Users/hagar/data/MovieSummaries/plot_summaries_small.tar.gz");

        try {
            String text = getTextFromTar(inputFile, 500 * 1000 * 1000);
            BottomUpTest t = new BottomUpTest(text, 40000, 3, true);
            t.buildVocabulary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Symbol {
        String value;
        int count;
        public final int id;

        public Symbol(String value, int count, int id) {
            this.value = value;
            this.count = count;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Symbol{" +
                    "value='" + value + '\'' +
                    ", count=" + count +
                    ", id=" + id +
                    '}';
        }
    }
}
