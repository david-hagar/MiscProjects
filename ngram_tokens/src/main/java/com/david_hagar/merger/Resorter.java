package com.david_hagar.merger;

import com.david_hagar.merger.string_counter.StringCount;
import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.token_scanner.Config;
import com.david_hagar.util.RateStats;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class Resorter {

    public static void resort(File workingFolder, MergableIO mergableIO) throws IOException, InterruptedException {
        RateStats stats = new RateStats(2000, "ngrams");
        final File inJoinFile = Config.getLastJoinFile(Config.getNGramFolder(workingFolder));
        try (DataInputStream in = MergableIO.openIn(inJoinFile)) {
            final File countFolder = Config.getCountFolder(workingFolder);
            MergerBufferWriter writer = new MergerBufferWriter(new StringCountIO(), countFolder, Config.MERGE_BUFFER_SIZE, new Comparator<Mergable>() {
                @Override
                public int compare(Mergable o1, Mergable o2) {
                    return -Integer.compare(((StringCount) o1).count, ((StringCount) o2).count);
                }
            });
            BackgroundJoiner j = new BackgroundJoiner(countFolder, Config.BACKGROUND_JOINER_BATCH_SIZE);
            j.start();
            try {
                final File countSortFile = Config.getCountSortFile(workingFolder);

                Mergable m;
                while ((m = mergableIO.read(in)) != null) {
                    final StringCount sc = (StringCount) m;

                    if (sc.count > 1) {
                        sc.count *= sc.value.length();
                        writer.add(m);
                    }
                    //System.out.println(m);
                    stats.oneEntryProcessed(m.getKey().length(), sc.toString());
                }
            } finally {
                writer.close();
            }


            inJoinFile.delete();

            j.joinAllFilesAndStop();

            stats.printEndStats();
        }
    }
}