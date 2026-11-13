package assignment2;

import java.util.*;
public class PlaylistBenchmark {
    public static void main(String[] args) {
        int n = 100000;
        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        long start, end;
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            arrayList.add(i);
        }
        end = System.nanoTime();
        System.out.println("ArrayList Insertion: " + (end - start) + " ns");
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            linkedList.add(i);
        }
        end = System.nanoTime();
        System.out.println("LinkedList Insertion: " + (end - start) + " ns");
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            arrayDeque.add(i);
        }
        end = System.nanoTime();
        System.out.println("ArrayDeque Insertion: " + (end - start) + " ns");
        start = System.nanoTime();
        while (!arrayList.isEmpty()) {
            arrayList.remove(0);
        }
        end = System.nanoTime();
        System.out.println("ArrayList Front Removal: " + (end - start) + " ns");
        start = System.nanoTime();
        while (!linkedList.isEmpty()) {
            linkedList.removeFirst();
        }
        end = System.nanoTime();
        System.out.println("LinkedList Front Removal: " + (end - start) + " ns");
        start = System.nanoTime();
        while (!arrayDeque.isEmpty()) {
            arrayDeque.removeFirst();
        }
        end = System.nanoTime();
        System.out.println("ArrayDeque Front Removal: " + (end - start) + " ns");
    }
}
