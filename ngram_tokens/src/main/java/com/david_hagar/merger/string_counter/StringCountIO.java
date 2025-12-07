package com.david_hagar.merger.string_counter;

import com.david_hagar.merger.Mergable;
import com.david_hagar.merger.MergableIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class StringCountIO implements MergableIO {

    public StringCountIO() {
    }

    @Override
    public Mergable read(DataInputStream inputStream) throws IOException {
        try {
            String value = inputStream.readUTF();
            int count = inputStream.readInt();
            return new StringCount(value, count);
        } catch (EOFException e) {
            inputStream.close();
            return null;
        }
    }

    @Override
    public void write(Mergable mergable, DataOutputStream outputStream) throws IOException {
        StringCount m = (StringCount) mergable;
        outputStream.writeUTF(m.value);
        outputStream.writeInt(m.count);
    }
}
