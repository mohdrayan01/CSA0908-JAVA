package assignment2;

import java.util.*;

public class UpNextRedesign {
    public static void main(String[] args) {
        int n = 100000;
        List<Integer> oldQueue = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            oldQueue.add(i);
        }
        long start = System.nanoTime();
        while (!oldQueue.isEmpty()) {
            oldQueue.remove(0);
        }
        long end = System.nanoTime();
        System.out.println(
            "ArrayList Removal Time: " + (end - start) + " ns"
        );
        Deque<Integer> newQueue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            newQueue.addLast(i);
        }
        start = System.nanoTime();
        while (!newQueue.isEmpty()) {
            newQueue.removeFirst();
        }
        end = System.nanoTime();
        System.out.println(
            "ArrayDeque Removal Time: " + (end - start) + " ns"
        );
        System.out.println("\nQueue Redesign Completed.");
    }
}
