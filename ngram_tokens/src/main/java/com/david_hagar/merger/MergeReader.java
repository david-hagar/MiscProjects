package com.david_hagar.merger;

import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.token_scanner.Config;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class MergeReader {


    private final MergableIO mergableIO;
    private final PriorityQueue<ReaderWrapper> minHeap;
    private final ArrayList<File> mergeFiles = new ArrayList<>();

    private class ReaderWrapper implements Comparable<ReaderWrapper> {

        private final DataInputStream dis;
        public Mergable mergable;

        public ReaderWrapper(DataInputStream dis) throws IOException {
            this.dis = dis;
            readNext();
        }

        public void readNext() throws IOException {
            mergable = mergableIO.read(dis);
        }

        @Override
        public int compareTo(ReaderWrapper o) {
            return mergable.getKey().compareTo(o.mergable.getKey());
        }
    }


    public MergeReader(File workingFolder, MergableIO mergableIO, File [] files) throws IOException {
        this.mergableIO = mergableIO;



        minHeap = new PriorityQueue<>(files.length);


        for (File file : files) {
            if( file.toString().endsWith(Config.MERGE_EXTENSION)) {
                DataInputStream dis = MergableIO.openIn(file);
                final ReaderWrapper readerWrapper = new ReaderWrapper(dis);
                if (readerWrapper.mergable != null)
                    minHeap.add(readerWrapper);
                mergeFiles.add(file);
            }
        }
    }

    private Mergable read() throws IOException {
        ReaderWrapper readerWrapper = minHeap.poll();
        if( readerWrapper == null)
            return null;

        Mergable retVal = readerWrapper.mergable;
        if( retVal != null){
            readerWrapper.readNext();
            if (readerWrapper.mergable != null)
                minHeap.add(readerWrapper);
            else
                readerWrapper.dis.close();
        }

        return retVal;
    }

    public Mergable readAndMerge() throws IOException {
        Mergable m = read();
        while(  !minHeap.isEmpty() && minHeap.peek().mergable.getKey() .equals( m.getKey()))
            m.merge(read());
        return m;
    }

    public ArrayList<File> getMergeFiles(){return mergeFiles;};

//    public static void main(String[] args) {
//        try {
//            File workingFolder = new File("/Users/hagar/data/working");
//            MergeReader r = new MergeReader(workingFolder, new StringCountIO(), );
//
//            Mergable m;
//            while ((m = r.readAndMerge()) != null) {
//                System.out.println(m);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
