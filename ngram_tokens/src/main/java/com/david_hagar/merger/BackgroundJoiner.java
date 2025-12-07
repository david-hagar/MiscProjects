package com.david_hagar.merger;

import com.david_hagar.merger.string_counter.StringCountIO;
import com.david_hagar.token_scanner.Config;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BackgroundJoiner {

    private final File mergeFolder;
    private final int batchSize;

    private Thread backgroundThread;
    int joinOutIndex = 1;

    public BackgroundJoiner(File mergeFolder, int batchSize) {
        this.mergeFolder = mergeFolder;
        this.batchSize = batchSize;

        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(!backgroundThread.isInterrupted()){
                    join(true);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                System.out.println("BackgroundJoiner Exiting.");
            }
        }, "Join Background");

    }


    private static class FileSize{
        public File file;
        public long size;

        public FileSize(File file) {
            this.file = file;
            this.size = file.length();
        }
    }

    private File [] nSmallest(File[] files, int n){

        if( n>= files.length)
            return files;

        PriorityQueue<FileSize> q = new PriorityQueue<>(new Comparator<FileSize>() {
            @Override
            public int compare(FileSize o1, FileSize o2) {
                return Long.compare(o1.size, o2.size);
            }
        });

        for (File file : files)
            q.add(new FileSize(file));

        File [] ret = new File[n];
        for (int i = 0; i < n; i++) {
            ret[i]=q.poll().file;
        }

        return ret;
    }



    private void join(boolean batch) {
        try {
            final File[] mergeFiles = Config.getMergeFiles(mergeFolder);
            if( mergeFiles.length >1) {
                final File[] srcFiles = batch ? nSmallest(mergeFiles, batchSize) : mergeFiles;
                Joiner.join(mergeFolder, new StringCountIO(), srcFiles, Config.getJoinFile(mergeFolder, joinOutIndex++));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start(){
        backgroundThread.start();
    }

    public void joinAllFilesAndStop() throws InterruptedException {
        backgroundThread.interrupt();
        backgroundThread.join(60*60*1000);
        System.out.println("Final Join.");
        join(false);
    }


}
