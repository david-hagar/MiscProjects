package com.david_hagar;

import com.david_hagar.merger.BackgroundJoiner;
import com.david_hagar.merger.MergerBufferWriter;
import com.david_hagar.merger.Resorter;
import com.david_hagar.merger.string_counter.StringCount;
import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.token_scanner.*;
import com.david_hagar.util.RateStats;
import com.david_hagar.util.TarProcessor;
import com.sun.management.UnixOperatingSystemMXBean;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Main {


    public static long printOpenFiles() {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof UnixOperatingSystemMXBean) {
            final long openFileDescriptorCount = ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount();
            System.out.println("Number of open fd: " + openFileDescriptorCount);
            return openFileDescriptorCount;
        }

        return -1;
    }

    private static void prepWorking(File workingFolder) throws IOException {

        FileUtils.deleteDirectory(workingFolder);
        workingFolder.mkdirs();
        Config.getNGramFolder(workingFolder).mkdirs();
        Config.getCountFolder(workingFolder).mkdirs();
    }


    public static void main(String[] args) {
        long of1 = printOpenFiles();
        System.out.println("Start Main");

        File workingFolder = new File("/Users/hagar/data/working");

        //File inputFile = new File("/Users/hagar/data/enron_mail_20150507.tar.gz");
        //File inputFile = new File("/Users/hagar/data/small.tar.gz");
        //File inputFile = new File("/Users/hagar/data/med.tar.gz");
        //File inputFile = new File("/Users/hagar/data/one/one.tar.gz");
        //File inputFile = new File("/Users/hagar/data/MovieSummaries/plot_summaries.tar.gz");
        File inputFile = new File("/Users/hagar/data/generated.tar.gz");
        //File inputFile = new File("/Users/hagar/data/MovieSummaries/plot_summaries_small.tar.gz");


        try {
            prepWorking(workingFolder);

            File nGramDir = Config.getNGramFolder(workingFolder);

            BloomCount bc = new BloomCount(Config.BLOOM_BUFFER_SIZE);
            TarProcessor.unTarFile(inputFile, new TarProcessor.DocVisitor() {
                @Override
                public boolean visit(String doc, TarArchiveEntry tarEntry, RateStats stats) throws IOException {
                    if (doc.length() == 0)
                        System.out.println("zero length document");
                    // System.out.println(body);

                    NgramGenerator.generate(doc, new NgramGenerator.NGramVisitor() {
                        @Override
                        public void visit(String ngram) throws IOException {
                            //System.out.println('\'' + ngram + '\'');
                            //writer.add(new StringCount(ngram, 1));
                            bc.addStringCount(ngram, 1);
                        }
                    }, Config.MIN_NGRAM_LENGTH, Config.MAX_NGRAM_LENGTH);

                    stats.oneEntryProcessed(tarEntry.getRealSize(), tarEntry.getName());
                    return true;
                }
            });

            bc.convertToFilter(Config.TOPN_SIZE, Config.BLOOM_MARGIN_FRACTION);

            BackgroundJoiner j = new BackgroundJoiner(nGramDir, Config.BACKGROUND_JOINER_BATCH_SIZE);
            j.start();

            MergerBufferWriter writer = new MergerBufferWriter(new StringCountIO(), Config.getNGramFolder(workingFolder), Config.MERGE_BUFFER_SIZE);
            try {
                TarProcessor.unTarFile(inputFile, new TarProcessor.DocVisitor() {
                    @Override
                    public boolean visit(String doc, TarArchiveEntry tarEntry, RateStats stats) throws IOException {
                        if (doc.length() == 0)
                            System.out.println("zero length document");

                        NgramGenerator.generate(doc, new NgramGenerator.NGramVisitor() {
                            @Override
                            public void visit(String ngram) throws IOException {
                                if(bc.pass(ngram))
                                 writer.add(new StringCount(ngram, 1));
                            }
                        }, Config.MIN_NGRAM_LENGTH, Config.MAX_NGRAM_LENGTH);

                        stats.oneEntryProcessed(tarEntry.getRealSize(), tarEntry.getName());
                        return true;
                    }
                });
            } finally {
                writer.close();
            }
            long of2 = printOpenFiles();
            j.joinAllFilesAndStop();

            Resorter.resort(workingFolder, new StringCountIO());
            ToText.toText(Config.getLastJoinFile(Config.getCountFolder(workingFolder)), Config.TOPN_SIZE);
            long of3 = printOpenFiles();

            System.out.println("extra open files = " + (of3 - of1));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
