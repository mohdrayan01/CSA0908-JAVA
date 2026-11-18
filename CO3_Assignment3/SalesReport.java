package Assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class SalesReport {
    private static int leakySum(Path file) throws IOException {
        BufferedReader reader = Files.newBufferedReader(file);
        int total = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            total += Integer.parseInt(line.trim());
        }

        reader.close();
        return total;
    }
    private static int sumManual(Path file) throws IOException {
        BufferedReader reader = null;
        int total = 0;
        try {
            reader = Files.newBufferedReader(file);
            String line;
            while ((line = reader.readLine()) != null) {
                total += Integer.parseInt(line.trim());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return total;
    }
    private static int sumAutomatic(Path file) throws IOException {
        int total = 0;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                total += Integer.parseInt(line.trim());
            }
        }
        return total;
    }
    private static void manualReport(Path[] files, Path output) {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(output);
            int total = 0;

            for (Path file : files) {
                total += sumManual(file);
            }
            writer.write("Total Sales: " + total);
            writer.newLine();
            System.out.println("Manual report generated");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Manual report failed: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println(
                        "Could not close report: " + e.getMessage()
                    );
                }
            }
        }
    }
    private static void automaticReport(Path[] files, Path output) {
        int total = 0;
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path file : files) {
                System.out.println("Reading " + file);
                total += sumAutomatic(file);
            }
            writer.write("Total Sales: " + total);
            writer.newLine();
            System.out.println("Automatic report generated");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Automatic report failed: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Path[] files = {
            Paths.get("store1.txt"),
            Paths.get("store2.txt")
        };
        manualReport(files, Paths.get("manual_report.txt"));
        automaticReport(files, Paths.get("automatic_report.txt"));
    }
}