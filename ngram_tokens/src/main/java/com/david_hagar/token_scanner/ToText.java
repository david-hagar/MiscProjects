package com.david_hagar.token_scanner;

import com.david_hagar.merger.Mergable;
import com.david_hagar.merger.MergableIO;
import com.david_hagar.merger.string_counter.StringCount;
import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.util.RateStats;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ToText {


    public static void toText(File mergeFile, int maxEntries) throws IOException {
        System.out.println("toText on " + mergeFile);
        RateStats stats = new RateStats(2000, "ngrams");
        StringCountIO mergableIO = new StringCountIO();

        final int[] outCount = {0};

        try (DataInputStream in = MergableIO.openIn(mergeFile)) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(mergeFile.toString() + ".txt")), StandardCharsets.UTF_8))) {
                SelectionFilter sf = new SelectionFilter(new SelectionFilter.SelectionVisitor() {
                    @Override
                    public void visit(StringCount sc) throws IOException {
                        final String escapedKey = sc.value.replace("\r", "\\r").replace("\n", "\\n");
                        bw.write('\'');
                        bw.write(escapedKey);
                        bw.write('\'');
                        bw.write(',');
                        bw.write(Integer.toString(sc.count));
                        bw.write(',');
                        bw.write(Integer.toString(sc.count/sc.value.length()));
                        bw.newLine();
                        stats.oneEntryProcessed(sc.value.length(), sc.toString());
                        outCount[0]++;
                    }
                });
                Mergable m;
                int entryCount = 0;
                while ((m = mergableIO.read(in)) != null) {
                    //System.out.println(m);
                    sf.putNext( (StringCount) m );
                    if( outCount[0] > maxEntries)
                        break;
                }
            }
        }
        stats.printEndStats();
    }


    public static void main(String[] args) {
        try {
            toText(new File("/Users/hagar/data/working/count-key/joined-8.merge"), 10 *1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
