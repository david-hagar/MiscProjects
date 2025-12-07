package com.david_hagar.merger;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;

public interface MergableIO {



     public Mergable read(DataInputStream inputStream) throws IOException;
     public void write(Mergable mergable, DataOutputStream outputStream) throws IOException;


     static DataOutputStream openOut(File f) throws IOException {
          return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f), 1024*1024));
     }

     static DataInputStream openIn(File f) throws IOException {
          return new DataInputStream(new BufferedInputStream(new FileInputStream(f), 1024*1024));
     }
}
