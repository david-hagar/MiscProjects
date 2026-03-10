package com.davidhagar.serialdata.gridwalk;

public class NestedArrays<T> {

    private final int nDim;
    private final int sizeOfEachDim;
    final Object[] store;

    public NestedArrays(int nDim, int sizeOfEachDim) {
        this.nDim = nDim;
        this.sizeOfEachDim = sizeOfEachDim;
        store = make(nDim, sizeOfEachDim);
    }

    public Object[] make(int nDim, int sizeOfEachDim){
        final Object[] o = new Object[sizeOfEachDim];
        if (nDim != 1) {
            for (int i = 0; i < sizeOfEachDim; i++) {
                o[i] = make(nDim - 1, sizeOfEachDim);
            }
        }
        return o;
    }



    public T get( int[] v){

        Object[] o = getLastArray(v);

        final int index = v[v.length - 1];
        if(index >= sizeOfEachDim)
            throw new IndexOutOfBoundsException("index out of bounds, index=" + index + " max=" + sizeOfEachDim + " dimension=" +index);

        //noinspection unchecked
        return (T) o[index];
    }

    public void set( int[] v, Boolean value){
        Object[] o = getLastArray(v);

        final int index = v[v.length - 1];
        if(index >= sizeOfEachDim)
            throw new IndexOutOfBoundsException("index out of bounds, index=" + index + " max=" + sizeOfEachDim + " dimension=" +index);
        o[index] = value;
    }
    private Object[] getLastArray(int[] v) {
        if(v.length != nDim)
            throw new IndexOutOfBoundsException("dimension length does not match, actual=" + v.length + " expected=" + nDim);

        Object [] o = store;
        for (int i = 0; i < v.length-1; i++) {
            final int index = v[i];
            if(index >= sizeOfEachDim)
                throw new IndexOutOfBoundsException("index out of bounds, index=" + index + " max=" + sizeOfEachDim + " dimension=" +i);
            o =  (Object[]) o[index];
        }
        return o;
    }


}
