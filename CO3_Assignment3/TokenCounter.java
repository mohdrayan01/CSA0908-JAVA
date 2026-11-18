package Assignment3;

public class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0;
    private int in = 0;
    private int out = 0;
    public synchronized void put(int token)
            throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll();
    }
    public synchronized int take()
            throws InterruptedException {
        while (count == 0) {
            wait();
        }
        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll();
        return token;
    }
    public static void main(String[] args) throws InterruptedException {
        TokenCounter counter = new TokenCounter();
        int[] collected = new int[2];
        Thread cook = new Thread(() -> {
            try {
                for (int token = 1; token <= 20; token++) {
                    counter.put(token);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread[] students = new Thread[2];
        for (int s = 0; s < students.length; s++) {
            final int id = s;
            students[s] = new Thread(() -> {
                try {
                    for (int k = 0; k < 10; k++) {
                        collected[id] += counter.take();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        cook.start();
        for (Thread student : students) {
            student.start();
        }
        cook.join();
        for (Thread student : students) {
            student.join();
        }
        int total = collected[0] + collected[1];
        System.out.println(
            "Sum of tokens collected: " + total + " (expected 210)"
        );
    }
}
