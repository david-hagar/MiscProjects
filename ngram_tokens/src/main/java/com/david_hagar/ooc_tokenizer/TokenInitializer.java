package com.david_hagar.ooc_tokenizer;

import com.david_hagar.ooc_tokenizer.pipe.Pipeline;
import com.david_hagar.ooc_tokenizer.pipe.TokenWriter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class TokenInitializer {

    private static final int EOF = -1;

    private final File[] inFiles;
    private final File tokenFolder;
    private final int maxTokensPerFile;
    private int tokenCount = 0;

    private int tokenFileCount = 0;
    private TokenWriter writer = null;
    private HashMap<Integer,Integer> tokenLookup = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public TokenInitializer(File[] inFiles, File tokenFolder, int maxTokensPerFile) throws IOException {

        this.inFiles = inFiles;
        this.tokenFolder = tokenFolder;
        this.maxTokensPerFile = maxTokensPerFile;

        FileUtils.deleteDirectory(tokenFolder);
        tokenFolder.mkdirs();
    }

    private void closeTokenFile() throws IOException {
        writer.close();
    }

    private void openTokenFile() throws FileNotFoundException {
        writer = new TokenWriter(new File("tokens" + tokenFileCount++));
    }


    public String [] process() throws IOException {

        Pipeline p = new Pipeline();
        //p.add();

        openTokenFile();

        for (File inFile : inFiles) {

            try (FileInputStream fis = new FileInputStream(inFile);
                 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)
            ) {

                int c;
                while ((c = reader.read()) != -1) {

                    writer.processToken(c);
                }

            }
        }

        return null;
    }



    /*


            try (DataInputStream ds = new Stream(new BufferedInputStream(new FileInputStream(inFile), 1024 * 100))) {
                int tokenID;
                while ((tokenID = ds.readShort()) != -1) {
                    visitor.visit(tokenID);
                }
            }
        }
     */

}
