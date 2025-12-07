package com.davidhagar.parrallel_test;



import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.sin;

public class ParrallelTest {

    public static void main(String[] args) {
        int[] numbers = new int[10_000_000]; // A large array for demonstration
        // Populate the array with some values
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i % 100; // Example: values from 0 to 99
        }

        // --- Sequential processing ---
        long startTimeSequential = System.nanoTime();
        double sumSequential = Arrays.stream(numbers)
                .mapToDouble(n -> sin(n * 2)) // Double each number
                .sum();          // Calculate the sum
        long endTimeSequential = System.nanoTime();
        System.out.println("Sequential sum: " + sumSequential);
        System.out.println("Sequential time: " + (endTimeSequential - startTimeSequential) / 1_000_000.0 + " ms");

        // --- Parallel processing ---
        long startTimeParallel = System.nanoTime();
        double sumParallel = Arrays.stream(numbers)
                .parallel()     // Enable parallel processing
                .mapToDouble(n -> sin(n * 2) ) // Double each number
                .sum();          // Calculate the sum
        long endTimeParallel = System.nanoTime();
        System.out.println("Parallel sum: " + sumParallel);
        System.out.println("Parallel time: " + (endTimeParallel - startTimeParallel) / 1_000_000.0 + " ms");

        // Another example: Modifying array elements in parallel (use with caution for stateful operations)
        int[] modifiedNumbers = new int[numbers.length];
        IntStream.range(0, numbers.length)
                .parallel()
                .forEach(i -> modifiedNumbers[i] = numbers[i] * 3); // Triple each number and store in a new array

        System.out.println("First 5 elements of modified array (parallel):");
        for (int i = 0; i < 5; i++) {
            System.out.print(modifiedNumbers[i] + " ");
        }
        System.out.println();
    }
}