package com.david_hagar.ooc_tokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class OocTokenizer {


    private final File inputFolder;
    private final File outputFolder;

    public OocTokenizer(File inputFolder, File outputFolder) {

        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
    }

    public static File getTokenFolder(File outFolder) {
        return new File(outFolder, "tokens");
    }

    public static File getNextFolder(File outFolder) {
        return new File(outFolder, "next");
    }

    public String[] getTokens() throws IOException {

        setupDirs();

        File[] inFiles = FileUtils.listFiles(inputFolder, new SuffixFileFilter("."), new DirectoryFileFilter() {
        }).toArray(new File[0]);
        System.out.println(inFiles.length + " files");


        TokenInitializer ti = new TokenInitializer(inFiles, getTokenFolder(outputFolder), 1000 * 1000 * 4);


        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setupDirs() throws IOException {
        FileUtils.deleteDirectory(outputFolder);
        outputFolder.mkdir();
        getTokenFolder(outputFolder).mkdir();
        getNextFolder(outputFolder).mkdir();
    }




    public static void main(String[] args) {
        try {
            File indir = new File(args[0]);
            File outdir = new File(args[1]);

            OocTokenizer ot = new OocTokenizer(indir, outdir);
            String[] tokens = ot.getTokens();
            for (String t : tokens) {
                System.out.println(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
