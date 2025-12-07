package com.david_hagar.ooc_tokenizer.trash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;

public class ThreadedFileVisitor {

    public static interface Visitor{
        public void visit(File f);
    }


    public static void visit(File files[], Visitor visitor, ThreadFactory tf){

        SynchronousQueue<File> queue = new SynchronousQueue<>();
        queue.addAll(List.of(files));
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread t = tf.newThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true){
                            File f = queue.poll();
                            if(f == null)
                                break;
                            visitor.visit(f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setName("Thread" +i);
            threads.add(t);
            t.start();

        }

        System.out.println("Created " + threads.size() + " threads.");

        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.println(thread.getName() + " finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
