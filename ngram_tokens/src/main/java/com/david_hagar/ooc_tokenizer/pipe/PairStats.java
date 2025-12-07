package com.david_hagar.ooc_tokenizer.pipe;

import com.david_hagar.ooc_tokenizer.TokenPairStats;

import java.io.IOException;

public class PairStats implements TokenProcessor{
    TokenPairStats stats = new TokenPairStats();

    int last = -1;

    @Override
    public void processToken(int tokenID) throws IOException {

        if(last !=-1){
            stats.incrementPair(last, tokenID);
            last = tokenID;
        }
    }
}
