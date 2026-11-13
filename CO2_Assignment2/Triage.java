package assignment2;

import java.util.*;
class Patient {
    String name;
    int severity;
    int arrivalOrder;
    Patient(String name, int severity, int arrivalOrder) {
        this.name = name;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
    }
    public String toString() {
        return name + " - Severity: " + severity;
    }
}
public class Triage {
    public static void main(String[] args) {
        PriorityQueue<Patient> queue =
                new PriorityQueue<>(
                    Comparator.comparingInt((Patient p) -> p.severity)
                    .reversed()
                    .thenComparingInt(p -> p.arrivalOrder)
                );
        queue.add(new Patient("Ravi", 2, 1));
        queue.add(new Patient("Arun", 5, 2));
        queue.add(new Patient("Kumar", 3, 3));
        queue.add(new Patient("Ajay", 5, 4));
        queue.add(new Patient("Vijay", 1, 5));
        queue.add(new Patient("Rahul", 4, 6));
        queue.add(new Patient("John", 3, 7));
        queue.add(new Patient("David", 5, 8));
        System.out.println("Patient Treatment Order:");
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
        System.out.println("\nPerformance Testing:");
        int[] sizes = {1000, 5000, 20000};
        for (int size : sizes) {
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            long start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                pq.add(i);
            }
            while (!pq.isEmpty()) {
                pq.poll();
            }
            long end = System.nanoTime();
            System.out.println(
                size + " patients: " + (end - start) + " ns"
            );
        }
    }
}
