package com.david_hagar.ooc_tokenizer.pipe;

import java.io.IOException;
import java.util.ArrayList;

public class Pipeline implements TokenProcessor{

    ArrayList<TokenPipelineProcessor> processors = new ArrayList<>();
    TokenPipelineProcessor last = null;
    TokenPipelineProcessor first = null;

    public Pipeline() {
    }

    public Pipeline add(TokenPipelineProcessor processor){

        processors.add(processor);

        if(last == null)
            first = processor;
        else
            last.setNext(processor);
        last = processor;
        return this;
    }

    public void processToken(int tokenID) throws IOException {
        first.processToken(tokenID);
    }


    public void close() throws IOException {
        for (TokenPipelineProcessor processor : processors) {
            processor.close();
        }
    }
}
