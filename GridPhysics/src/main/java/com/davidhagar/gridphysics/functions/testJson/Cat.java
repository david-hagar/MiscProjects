package com.davidhagar.gridphysics.functions.testJson;

public class Cat extends Animal {
    public int livesRemaining;
    // ... other Cat-specific properties


    public Cat() {
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    public void setLivesRemaining(int livesRemaining) {
        this.livesRemaining = livesRemaining;
    }
}
