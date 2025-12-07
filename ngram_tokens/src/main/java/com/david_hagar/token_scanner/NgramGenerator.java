package com.david_hagar.token_scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class NgramGenerator {
    public static interface NGramVisitor {
        public void visit(String ngram) throws IOException;
    }

    public static void generate(String doc, NGramVisitor visitor, int minLength, int maxLength) throws IOException {

        char buffer[] = new char[maxLength];

        for (int i = 0; i < maxLength; i++)
            buffer[i] = 0;

        int bufferSize = 0;

        int length = doc.length();
        for (int i = 0; i < length; i++) {
            if (bufferSize == maxLength)
                for (int j = 1; j < bufferSize; j++) {
                    buffer[j - 1] = buffer[j];
                }
            bufferSize++;
            if (bufferSize > maxLength)
                bufferSize = maxLength;
            buffer[bufferSize - 1] = doc.charAt(i);

            for (int j = minLength; j <= bufferSize; j++) {
                visitor.visit(new String(buffer, bufferSize-j, j));
            }
            //System.out.println();
        }


    }

    private static void test(ArrayList<String> expected,  int minLength, int maxLength) throws IOException {
        ArrayList<String> actual = new ArrayList<>();
        generate("hello", new NGramVisitor() {
            @Override
            public void visit(String ngram) {
                //System.out.println(ngram);
                actual.add(ngram);
            }
        }, minLength, maxLength);
        Collections.sort(actual);
        Collections.sort(expected);
        if( !expected.equals(actual))
        {
            System.out.println("actual: " + actual);
            System.out.println("expected: " + expected);
            throw new RuntimeException("Fail");
        }
    }

    private static void test() throws IOException {
        String [] ones = {"h", "e", "l", "l", "o"};
        String [] twos = {"he", "el", "ll", "lo"};
        String [] threes = {"hel", "ell", "llo"};

        ArrayList<String> expected = new ArrayList<>(Arrays.asList(threes));

        test(expected, 3, 3);
        expected.addAll(Arrays.asList(twos));
        test(expected, 2, 3);
        expected.addAll(Arrays.asList(ones));
        test(expected, 1, 3);

        expected = new ArrayList<>(Arrays.asList(ones));
        test(expected, 1, 1);

        expected = new ArrayList<>(Arrays.asList(twos));
        test(expected, 2, 2);

    }



    public static void main(String[] args) {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
