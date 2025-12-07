package com.david_hagar.util;

import com.david_hagar.util.RateStats;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class TarProcessor {

    public static interface DocVisitor{
        public boolean visit( String doc, TarArchiveEntry tarEntry, RateStats stats) throws IOException;
    }



    public static void unTarFile(File tarFile, DocVisitor docVisitor) throws IOException, InterruptedException {

        RateStats stats = new RateStats(2000, "docs");

        TarArchiveInputStream tis = null;
        try {
            FileInputStream fis = new FileInputStream(tarFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));
            tis = new TarArchiveInputStream(gzipInputStream);
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tis.getNextTarEntry()) != null) {
                if (!tarEntry.isDirectory() && ! new File(tarEntry.getName()).getName().startsWith(".")) {
                    //System.out.println(" tar entry- " + tarEntry.getName());
                    byte[] bytes = tis.readAllBytes();
                    String s = new String(bytes, StandardCharsets.UTF_8);
                    //System.out.println(s);
                    if( !docVisitor.visit(s, tarEntry, stats))
                        break;
                }
            }
        } finally {
            if (tis != null)
                    tis.close();
        }
        stats.printEndStats();
        System.out.println("Finished Reading Tar " + tarFile);
    }



}



