package com.david_hagar.gen;

import com.david_hagar.util.RateStats;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class TextGenerator {


    public static final int EARLY_TERMINATION_COUNT = 3;

    public static void generateText(File outFolder, int topWordCount, int nUniqueWords, int wordsPerFile, float nextMultiplier) throws Exception {

        if (nextMultiplier >= 1)
            throw new Exception("nextMultiplier must be less tha 1.0. value = " + nextMultiplier);

        FileUtils.deleteDirectory(outFolder);
        outFolder.mkdirs();

        WeightedRandom<StringGoalCount> wr = new WeightedRandom<>();

        double nextCount = topWordCount;

        int totalWordCount = 0;
        try (PrintWriter pw = new PrintWriter(new File(outFolder.toString() + "_voc.txt"), StandardCharsets.UTF_8)) {
            for (int i = 0; i < nUniqueWords; i++) {
                int goalCount = (int) Math.round(nextCount);
                StringGoalCount sgc = new StringGoalCount("w_" + i + "_" + goalCount, 0, goalCount);

                wr.add(sgc.goalCount, sgc);
                totalWordCount += goalCount;

                nextCount *= nextMultiplier;
                if (nextCount < 1) {
                    System.out.println("Ended early. Count less than 1 reached");
                    break;
                }

                pw.println(sgc.value + ", " + sgc.goalCount);
            }
        }
        System.out.println("totalWordCount = " + totalWordCount);

        RateStats rs = new RateStats(2000, "words");
        int fileCount = 0;
        while (totalWordCount > EARLY_TERMINATION_COUNT) {
            File outFile = new File(outFolder, fileCount++ + ".txt");
            try (PrintWriter pw = new PrintWriter(outFile, StandardCharsets.UTF_8)) {
                for (int i = 0; i < wordsPerFile; i++) {
                    StringGoalCount sgc = wr.next();
                    if (sgc.count < sgc.goalCount) {
                        sgc.count++;
                        pw.print(' ');
                        pw.print(sgc.value);
                        totalWordCount--;
                        rs.oneEntryProcessed(sgc.value.length(), sgc.toString() + " remaining=" + totalWordCount);
                    } else
                        i--;
                    if( totalWordCount <= EARLY_TERMINATION_COUNT)
                        break;
                }

            }
        }


    }


    public static void main(String[] args) {

        try {
            generateText(new File("/Users/hagar/data/generated"), 5000, 1000, 10000, 0.99f);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
