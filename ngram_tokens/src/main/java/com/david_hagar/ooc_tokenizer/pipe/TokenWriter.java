package com.david_hagar.ooc_tokenizer.pipe;

import java.io.*;

public class TokenWriter extends TokenPipelineProcessor {

    private final DataOutputStream ds;

    public TokenWriter(File f) throws FileNotFoundException {
        ds = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f), 1024 * 100));
    }


    @Override
    public void processToken(int tokenID) throws IOException {
        ds.writeShort(tokenID);
        next.processToken(tokenID);
    }

    @Override
    public void close() throws IOException {
        ds.close();
    }
}
