package com.davidhagar.gridphysics.util;

import java.util.Random;

public class RandomUtil {

    public static Random random = new Random(999);


    public static int rInt(int min, int max) {
        return min + (int) (random.nextFloat() * ((max - min) + 1));   // inclusive at min and max
    }

    public static int rChange(int value) {
        return random.nextFloat() > 0.5f ? value + 1 : value - 1;
    }

    public static int rChangeLimit(int value, int min, int max) {
        if( min == max)
            return min;

        int v = rChange(value);
        if (v > max)
            v = max;
        else if (v < min)
            v = min;
        return v;
    }

    public static int rChangeLimitExclude(int value, int min, int max) {
        if( min == max)
            return min;

        while (true) {
            int i = rChangeLimit(value, min, max);
            if (i != value)
                return i;
        }
    }

    public static int rIntExclude(int exclude, int min, int max) {
        if( min == max)
            return min;

        while (true) {
            int v = rInt(min, max);
            if (v != exclude)
                return v;
        }

    }

    public static float rFloat(float min, float max) {
        return (float) (min + random.nextFloat() * (max - min));
    }

    public static double rDouble(float min, float max) {
        return (double) (min + random.nextFloat() * (max - min));
    }
}
