package com.david_hagar.ooc_tokenizer;

import java.util.Objects;

public class TokenPairCount {

    int firstId;
    int secondId;
    public int count = 0;


    public TokenPairCount(int firstId, int secondId) {
        this.firstId = firstId;
        this.secondId = secondId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenPairCount)) return false;
        TokenPairCount tokenPairCount = (TokenPairCount) o;
        return firstId == tokenPairCount.firstId && secondId == tokenPairCount.secondId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstId, secondId);
    }
}
