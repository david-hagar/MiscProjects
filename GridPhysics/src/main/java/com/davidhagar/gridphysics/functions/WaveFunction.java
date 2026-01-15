package com.davidhagar.gridphysics.functions;

import com.davidhagar.gridphysics.GridStats;
import com.davidhagar.gridphysics.State;
import com.davidhagar.gridphysics.util.Util;

import javax.swing.*;
import java.awt.*;

// https://www.google.com/search?q=how+to+implement+wave+in+a+descrete+2d+array&oq=how+to+implement+wave+in+a+descrete+2d+array&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTIJCAEQIRgKGKABMgkIAhAhGAoYoAEyCQgDECEYChigATIJCAQQIRgKGKABMgkIBRAhGAoYqwIyBwgGECEYjwLSAQkxNjMyN2owajeoAgCwAgA&sourceid=chrome&ie=UTF-8#sv=CBASnQMK9gIKBtrZ29IPABLrAgoMCgq62dvSDwQgATgDCoMBCoABwtnb0g96engiIHNnZV9tc2FmX21mb2phZDJSR3JHc3F0c1AwZW5LZ0FVMBJyRkImChNmZWVkYmFja19lbnRyeXBvaW50Eg9zZ2VfbXNhZl9hY3Rpb26qAQxNQUdJX0ZFQVRVUkWyAQwKABIEQkxVUhgAIACgAU7gAQHoAQH4AQEK1AEK0QHK2dvSD8oBEqUBIqIBL3NlYXJjaC9hYm91dC10aGlzLXJlc3VsdD9vcmlnaW49d3d3Lmdvb2dsZS5jb20mY3M9MSZyZXE9Q2dBU013b25DZ0lJY1FvaENGMWlIUW9NWTI5MWJuUnllVjl1WVcxbEVnMVZibWwwWldRZ1UzUmhkR1Z6RWdRS0FnaEhHZ0lJQUJvQUlnSVFBVWdCV0FCb0FBJmhsPWVuLVVTJmdsPVVTGhZodHRwczovL3d3dy5nb29nbGUuY29tWgBgAWgAcAB4ABIiYXRyaXRlbS1fbWZvamFkMlJHckdzcXRzUDBlbktnQVVfNxgvIIOJxusBMAI


public class WaveFunction implements StateFunction {

    public static final int HISTORY_SIZE = 3;
    float CSquared = getCSquared();
    private static final boolean color = true;

    public WaveFunction() {

    }

    @Override
    public State[][] getInitialGrid() {

        int width = 512;
        int height = 512;
//        int width = 599;
//        int height = 601;
        State[][] values = new State[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                values[i][j] = new State(1, HISTORY_SIZE);
            }
        }


        //initRandom(width, height, values);
        initCenter(width, height, values);

        return values;
    }

    private static void initRandom(int width, int height, State[][] values) {
        int wo2 = width / 2;
        int h02 = height / 2;

        int spread = 200;
        for (int i = 0; i < 10; i++) {
            setPoint(values, (int)(wo2 + (Math.random() *2 -1) * spread), (int)(h02 + (Math.random() *2 -1) * spread));
        }
    }

    private static void initCenter(int width, int height, State[][] values) {
        int wo2 = width / 2;
        int h02 = height / 2;
        setPoint(values, wo2 , h02);
    }

    private static void setPoint(State[][] values, int i, int j) {
        values[i][j].values[0][0] = 1;
        values[i +1][j].values[0][0] = 1;
        values[i][j +1].values[0][0] = 1;
        values[i +1][j +1].values[0][0] = 1;
    }

    @Override
    public void update(State[][] grid, int i, int j) {

        int widthM1 = grid.length - 1;
        int heightM1 = grid[0].length - 1;

        int iM1 = i == 0 ? widthM1 : i - 1;
        int jM1 = j == 0 ? heightM1 : j - 1;
        int iP1 = i == widthM1 ? 0 : i + 1;
        int jP1 = j == heightM1 ? 0 : j + 1;


        // u(i, j, n+1) = 2*u(i, j, n) - u(i, j, n-1) + C² * (u(i+1, j, n) + u(i-1, j, n) + u(i, j+1, n) + u(i, j-1, n) - 4*u(i, j, n))
        float[][] values = grid[i][j].values;
        values[0][0] = 2 * values[1][0] - values[2][0] +
                CSquared * (grid[iP1][j].values[1][0] + grid[iM1][j].values[1][0] +
                        grid[i][jP1].values[1][0] + grid[i][jM1].values[1][0] -
                        4 * values[1][0]);

    }

    private static float getCSquared() {
        float c = 1.0f; // Wave speed
        float dt = 0.5f; // Time step
        float dx = 1.0f; // Spatial step (assuming dx = dy)
        float C = (c * dt / dx);
        float CSquared = C * C;

        System.out.println("CSquared = " + CSquared);
        System.out.println("1/sqrt(2) = " + 1/Math.sqrt(2));
        return CSquared;
    }

    @Override
    public int getRGB(State state, GridStats gridStats) {
        float[] v = state.values[0];
        float iFloat = (v[0] - gridStats.min[0]) / (gridStats.max[0] - gridStats.min[0]);
        if( color){
            return Color.HSBtoRGB(iFloat, 1, 1);
        }
        else {
            int i = (int) (iFloat * 255);
            return Util.convertRGBToInt(i, i, i);
        }

    }

    @Override
    public int getStateSize() {
        return 1;
    }

    public int getHistorySize(){
        return HISTORY_SIZE;
    }


    @Override
    public JPanel getControls() {
        return new JPanel();
    }

}
