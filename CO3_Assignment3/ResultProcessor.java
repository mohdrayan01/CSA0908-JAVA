package Assignment3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
public class ResultProcessor {
    static class Result {
        final String file;
        final int score;
        final String grade;
        Result(String file, int score) {
            this.file = file;
            this.score = score;
            if (score >= 90) {
                grade = "A";
            } else if (score >= 80) {
                grade = "B";
            } else if (score >= 70) {
                grade = "C";
            } else if (score >= 60) {
                grade = "D";
            } else {
                grade = "F";
            }
        }
    }
    static class Failure {
        final String file;
        final String message;
        Failure(String file, String message) {
            this.file = file;
            this.message = message;
        }
    }
    static Result process(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        if (lines.isEmpty()) {
            throw new IOException("Empty score file");
        }
        int score;
        try {
            score = Integer.parseInt(lines.get(0).trim());
        } catch (NumberFormatException e) {
            throw new IOException("Corrupt score", e);
        }
        if (score < 0 || score > 100) {
            throw new IOException("Score outside 0..100");
        }
        return new Result(
            file.getFileName().toString(),
            score
        );
    }
    public static void main(String[] args)
            throws InterruptedException {
        int workers = args.length > 0
            ? Integer.parseInt(args[0])
            : 4;
        List<Path> files = new ArrayList<>();
        try {
            for (String name :
                    Files.readAllLines(Paths.get("score-files.txt"))) {
                if (!name.trim().isEmpty()) {
                    files.add(Paths.get(name.trim()));
                }
            }
        } catch (IOException e) {
            System.err.println(
                "Cannot read score-files.txt: " + e.getMessage()
            );
            return;
        }
        ExecutorService pool =
            Executors.newFixedThreadPool(workers);
        ConcurrentLinkedQueue<Result> successes =
            new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Failure> failures =
            new ConcurrentLinkedQueue<>();
        List<Future<Result>> futures = new ArrayList<>();
        long start = System.nanoTime();
        for (Path file : files) {
            futures.add(pool.submit(() -> process(file)));
        }
        pool.shutdown();
        for (int i = 0; i < futures.size(); i++) {
            try {
                successes.add(futures.get(i).get());
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                failures.add(
                    new Failure(
                        files.get(i).toString(),
                        cause.getMessage()
                    )
                );
            }
        }
        pool.awaitTermination(1, TimeUnit.HOURS);
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(
            System.nanoTime() - start
        );
        System.out.println("Files attempted: " + files.size());
        System.out.println("Successful: " + successes.size());
        System.out.println("Failed: " + failures.size());
        System.out.println("Elapsed ms: " + elapsedMs);
        for (Failure failure : failures) {
            System.out.println(
                "FAILED " + failure.file + " - " + failure.message
            );
        }
    }
}