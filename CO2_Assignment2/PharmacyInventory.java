package assignment2;

import java.util.*;
public class PharmacyInventory {
    public static void main(String[] args) {
        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, String> treeMap = new TreeMap<>();
        Map<Integer, String> linkedMap = new LinkedHashMap<>();
        int[] codes = {104, 101, 103, 102, 105};
        String[] medicines = {
            "Paracetamol",
            "Aspirin",
            "Cetirizine",
            "Ibuprofen",
            "Vitamin C"
        };
        for (int i = 0; i < codes.length; i++) {
            hashMap.put(codes[i], medicines[i]);
            treeMap.put(codes[i], medicines[i]);
            linkedMap.put(codes[i], medicines[i]);
        }
        System.out.println("HashMap Inventory:");
        System.out.println(hashMap);
        System.out.println("\nTreeMap Inventory:");
        System.out.println(treeMap);
        System.out.println("\nLinkedHashMap Inventory:");
        System.out.println(linkedMap);
        int searchCode = 103;
        System.out.println("\nSearching Medicine:");
        if (hashMap.containsKey(searchCode)) {
            System.out.println("Medicine Found: "
                    + hashMap.get(searchCode));
        } else {
            System.out.println("Medicine Not Found");
        }
        System.out.println("\nSorted Inventory Report:");
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println("\nInsertion Order Report:");
        for (Map.Entry<Integer, String> entry : linkedMap.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}