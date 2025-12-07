package com.davidhagar.gridphysics.util;

public class Util {


    public static int convertRGBToInt(int red, int green, int blue) {
        // Ensure values are within the 0-255 range
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        // Combine using bit shifting and bitwise OR
        // Alpha is often set to 255 (fully opaque) for RGB ints
        return (255 << 24) | (red << 16) | (green << 8) | blue;
    }

}
