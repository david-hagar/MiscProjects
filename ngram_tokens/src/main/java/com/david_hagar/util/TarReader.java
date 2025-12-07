package com.david_hagar.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class TarReader {

    TarArchiveInputStream tis = null;
    private String currentString = null;
    private int currentStringIndex = -1;
    private ArrayList<Character> pushbackBuffer = new ArrayList<>();


    public TarReader(File tarFile) throws IOException {
        FileInputStream fis = new FileInputStream(tarFile);
        GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));
        tis = new TarArchiveInputStream(gzipInputStream);
    }


    public String readNextString() throws IOException {

        TarArchiveEntry tarEntry = null;
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            if (!tarEntry.isDirectory() && !new File(tarEntry.getName()).getName().startsWith(".")) {
                //System.out.println(" tar entry- " + tarEntry.getName());
                byte[] bytes = tis.readAllBytes();
                String s = new String(bytes, StandardCharsets.UTF_8);
                //System.out.println(s);

                return s;
            }
        }

        if (tis != null)
            tis.close();
        return null;
    }


    Character readNextChar() throws IOException {

        if (!pushbackBuffer.isEmpty())
            return pushbackBuffer.remove( pushbackBuffer.size()-1);

        if (currentString == null) {
            currentString = readNextString();
            if (currentString == null)
                return null;
            currentStringIndex = 0;
        }

        char c = currentString.charAt(currentStringIndex++);
        if (currentStringIndex >= currentString.length()) {
            currentStringIndex = -1;
            currentString = null;
        }

        return c;
    }


    public void pushBack( Character c){
        pushbackBuffer.add(c);
    }

}
