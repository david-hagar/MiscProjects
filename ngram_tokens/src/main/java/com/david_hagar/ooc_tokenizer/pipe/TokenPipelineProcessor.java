package com.david_hagar.ooc_tokenizer.pipe;

import com.david_hagar.ooc_tokenizer.TokenPairStats;

import java.io.File;
import java.io.IOException;

public abstract class TokenPipelineProcessor implements TokenProcessor {

    protected TokenPipelineProcessor next;

    public void setNext(TokenPipelineProcessor tpe){
        next = tpe;
    }

    public TokenPairStats processFile(File f) throws IOException{
        throw new IOException("not Implemented");
    }

    public abstract void processToken( int tokenID) throws IOException;

    public abstract void close() throws IOException;
}
