package com.david_hagar.merger;

import com.david_hagar.token_scanner.Config;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MergerBufferWriter {


    private final MergableIO mergableIO;
    private final File mergeFolder;
    private final int maxBufferSize;
    private final HashMap<String, Mergable> buffer;
    private int outFileCount = 0;
    private Comparator<Mergable> comparator;

    public MergerBufferWriter(MergableIO mergableIO, File mergeFolder, int maxBufferSize) {
        this(mergableIO, mergeFolder, maxBufferSize, new Comparator<Mergable>() {
            @Override
            public int compare(Mergable o1, Mergable o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
    }

    public MergerBufferWriter(MergableIO mergableIO, File mergeFolder, int maxBufferSize, Comparator<Mergable> comparator) {
        this.mergableIO = mergableIO;
        this.mergeFolder = mergeFolder;
        this.maxBufferSize = maxBufferSize;
        this.buffer = new HashMap<String, Mergable>(maxBufferSize);
        this.comparator = comparator;
    }

    public void add(Mergable mergable) throws IOException {
        String key = mergable.getKey();
        if (buffer.containsKey(key))
            buffer.get(key).merge(mergable);
        else {
            buffer.put(key, mergable);
            if (buffer.size() > maxBufferSize)
                flushBuffer();
        }
    }

    private void flushBuffer() throws IOException {
        System.out.println("Sorting buffer on " + Thread.currentThread().getName() + " ...");
        ArrayList<Mergable> toSort = new ArrayList<Mergable>(buffer.values());
        buffer.clear();
        Collections.sort(toSort, comparator);

        System.out.println("Writing buffer ...");

        File tmpFile = new File(mergeFolder, Thread.currentThread().getName() + " " + outFileCount++ + Config.WORKING_EXTENSION);
        try (DataOutputStream out = MergableIO.openOut(tmpFile)) {
            for (Mergable m : toSort) {
                mergableIO.write(m, out);
                //System.out.println(m);
            }
        }
        final String newFileName = tmpFile.toString().replace(Config.WORKING_EXTENSION, Config.MERGE_EXTENSION);
        tmpFile.renameTo(new File(newFileName));
        System.out.println("Flushing buffer done.");
    }

    public void close() throws IOException {
        flushBuffer();
    }

}
