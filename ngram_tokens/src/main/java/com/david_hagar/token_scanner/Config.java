package com.david_hagar.token_scanner;

import com.david_hagar.merger.MergableIO;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

public class Config {

    public static final int MIN_NGRAM_LENGTH = 2;
    public static final int MAX_NGRAM_LENGTH = 32;

    public static final int MERGE_BUFFER_SIZE = 16 * 1000 * 10;
    public static final int BLOOM_BUFFER_SIZE = 100 * 1000 * 1000;
    public static final float BLOOM_MARGIN_FRACTION = 0.3f;
    public static final int TOPN_SIZE = 100 * 1000;
    public static final int BACKGROUND_JOINER_BATCH_SIZE = 4;

    public static final String WORKING_EXTENSION = ".working";
    public static final String MERGE_EXTENSION = ".merge";

    public static File getNGramFolder(File workingFolder) {
        return new File(workingFolder, "ngram-key");
    }

    public static File getCountFolder(File workingFolder) {
        return new File(workingFolder, "count-key");
    }

    public static File getJoinFile(File folder, int index) {
        return new File(folder, "joined-"+ index + MERGE_EXTENSION);
    }
    public static File getCountSortFile(File folder) {
        return new File(folder, "countSort" + MERGE_EXTENSION);
    }

    public static File getLastJoinFile(File folder) throws IOException {
        File[] files = getMergeFiles(folder);
        if( files !=null && files.length == 1)
            return files[0];
        else
            throw new IOException("More merge files than expected." + String.join(", " , Arrays.toString(files)) );
    }

    public static File[] getMergeFiles(File folder) {
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(MERGE_EXTENSION);
            }
        });
        return files;
    }
}
