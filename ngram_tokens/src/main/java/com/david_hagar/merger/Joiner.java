package com.david_hagar.merger;

import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.token_scanner.Config;
import com.david_hagar.util.RateStats;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Joiner {

    public static void join(File workingFolder, MergableIO mergableIO, File[] srcFiles, File destFile) throws IOException {
        System.out.println("Joining " + srcFiles.length + " files ...");
        RateStats stats = new RateStats(2000, "ngrams");


        MergeReader r = new MergeReader(workingFolder, new StringCountIO(), srcFiles);
        File workingDestFile = new File(destFile.toString() + Config.WORKING_EXTENSION);
        try (DataOutputStream out = MergableIO.openOut(workingDestFile)) {
            Mergable m;
            while ((m = r.readAndMerge()) != null) {
                mergableIO.write(m, out);
                //System.out.println(m);
                stats.oneEntryProcessed(m.getKey().length(), m.toString());
            }
        }

        workingDestFile.renameTo(destFile);

        for (File f : r.getMergeFiles()) {
            f.delete();
            System.out.println("deleted " + f);
        }

        stats.printEndStats();
    }


//    public static void main(String[] args) {
//        try {
//            File workingFolder = new File("/Users/hagar/data/working");
//            Joiner.join(workingFolder, new StringCountIO(), 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
