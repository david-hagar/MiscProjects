package com.david_hagar.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class RateStats {

    private final int updateTimeInMs;
    private final String itemName;
    private int windowTotalItems = 0;
    private int totalItems = 0;
    private long totalBytesProcessed = 0;
    private long windowBytesProcessed = 0;
    private final long start = System.currentTimeMillis();
    private long lastUpdate = System.currentTimeMillis();

    public RateStats(int updateTimeInMs, String itemName) {
        this.updateTimeInMs = updateTimeInMs;
        this.itemName = itemName;
    }

    public void oneEntryProcessed(long bytesProcessed, Object currentItem) {
        String toPrint = null;
        synchronized (this) {
            windowTotalItems++;
            windowBytesProcessed += bytesProcessed;

            final long elapsedTime = System.currentTimeMillis() - lastUpdate;
            if (elapsedTime >= updateTimeInMs) {
                final float elapsedSec = elapsedTime / 1000.0f;
                float megPerSec = windowBytesProcessed / 1e6f / elapsedSec;
                float recordsPerSecond = windowTotalItems / elapsedSec;
                totalBytesProcessed += windowBytesProcessed;
                totalItems+=windowTotalItems;
                windowTotalItems = 0;
                windowBytesProcessed = 0;
                lastUpdate = System.currentTimeMillis();
                toPrint = String.format("%.4f M/sec, %.2f %s/sec, items:%d :%s", megPerSec, recordsPerSecond, itemName, totalItems, currentItem.toString());
            }
        }
         if (toPrint != null)
            System.out.println(toPrint);
    }

    public void printEndStats() {
        System.out.println("Total items processed = " + windowTotalItems);
        System.out.println("Total Mbytes processed = " + totalBytesProcessed/1e6f);
        System.out.println("Total time = " + DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "HH:mm:ss.S"));
    }


    public static void main(String[] args) {
        RateStats s = new RateStats(500, "test");

        for (int i = 0; i < 40; i++) {
            s.oneEntryProcessed(10000, "test " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }

        s.printEndStats();
    }


}
