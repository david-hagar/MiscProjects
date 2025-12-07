package com.david_hagar.util;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileSplit {


    public static void main(String[] args) {

        File inFile = new File("/Users/hagar/Desktop/MovieSummaries/plot_summaries.txt");
        File outDir = new File(inFile.getParentFile(), "out");

        BufferedReader reader;
        int i = 0;
        try {
            FileUtils.deleteDirectory(outDir);
            outDir.mkdirs();

            reader = new BufferedReader(new FileReader(inFile));
            String line;
            while ((line = reader.readLine()) != null) {
                int tabIndex = line.indexOf('\t');
                if (tabIndex != -1)
                    line = line.substring(tabIndex + 1, line.length());
                FileUtils.writeStringToFile(new File(outDir, i++ + ".txt"), line, StandardCharsets.UTF_8);
                // System.out.println(line);

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
