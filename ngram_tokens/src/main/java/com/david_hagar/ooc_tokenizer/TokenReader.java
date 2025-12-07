package com.david_hagar.ooc_tokenizer;

import java.io.*;


public class TokenReader {

    public static interface Visitor {
        public void visit(int tokenID);
    }


    public static void processFile(File f, Visitor visitor) throws IOException {

        try (DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(f), 1024 * 100))) {
            int tokenID;
            while ((tokenID = ds.readShort()) != -1) {
                visitor.visit(tokenID);
            }
        }
    }

}
