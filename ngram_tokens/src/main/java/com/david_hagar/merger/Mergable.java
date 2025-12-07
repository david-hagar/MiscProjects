package com.david_hagar.merger;

import java.io.OutputStream;

public interface Mergable {

    public String getKey();
    public void merge(Mergable mergable);

}
