package com.david_hagar.ooc_tokenizer.pipe;

import java.io.IOException;

public interface TokenProcessor {

    public void processToken( int tokenID) throws IOException;

}
