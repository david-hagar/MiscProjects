package com.david_hagar.ooc_tokenizer;

import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;

public class TokenPairStats {

    private HashMap<TokenPairCount, TokenPairCount > stats = new HashMap<>();

    public void incrementPair(int firstID, int secondID){
        TokenPairCount p = new TokenPairCount(firstID, secondID);
        TokenPairCount count = stats.get(p);
        if( count == null){
            count = p;
            stats.put(count,count);
        }
        count.count++;
    }

    public void merge(TokenPairStats other){

        for (TokenPairCount count : other.stats.values()) {
            TokenPairCount thisCount = stats.get(count);
            if( thisCount == null){
                thisCount = count;
                stats.put(thisCount,thisCount);
            }else
                thisCount.count+= count.count;
        }


    }
}
