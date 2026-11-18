package Assignment3;

public class WalletTransfer {
    static class Wallet {
        final int id;
        long balance;
        Wallet(int id, long balance) {
            this.id = id;
            this.balance = balance;
        }
    }
    static void transferUnsafe(Wallet from, Wallet to, long amount) {
        synchronized (from) {
            synchronized (to) {
                move(from, to, amount);
            }
        }
    }
    static void transfer(Wallet from, Wallet to, long amount) {
        Wallet first = from.id < to.id ? from : to;
        Wallet second = from.id < to.id ? to : from;
        synchronized (first) {
            synchronized (second) {
                move(from, to, amount);
            }
        }
    }
    private static void move(Wallet from, Wallet to, long amount) {
        if (amount < 0 || from.balance < amount) {
            throw new IllegalArgumentException("Invalid transfer");
        }
        from.balance -= amount;
        to.balance += amount;
    }
    public static void main(String[] args) throws InterruptedException {
        Wallet a = new Wallet(1, 1000);
        Wallet b = new Wallet(2, 1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                transfer(a, b, 1);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                transfer(b, a, 1);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Wallet A balance: " + a.balance);
        System.out.println("Wallet B balance: " + b.balance);
        System.out.println(
            "Total balance: " + (a.balance + b.balance)
        );
    }
}