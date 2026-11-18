package Assignment3;

public class SeatBooking {
    private final boolean[] booked;
    private int bookingsMade;
    SeatBooking(int seats) {
        booked = new boolean[seats];
    }
    public void bookWithoutSync(int seat) {
        if (!booked[seat]) {
            Thread.yield();
            booked[seat] = true;
            bookingsMade++;
        }
    }
    public synchronized void book(int seat) {
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
        }
    }
    public void bookWithBlock(int seat) {
        synchronized (this) {
            if (!booked[seat]) {
                booked[seat] = true;
                bookingsMade++;
            }
        }
    }
    public int getBookingsMade() {
        return bookingsMade;
    }
    private static void runTest(String version) throws InterruptedException {
        SeatBooking screen = new SeatBooking(10);
        Thread[] users = new Thread[100];
        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;
            users[i] = new Thread(() -> {
                switch (version) {
                    case "unsafe":
                        screen.bookWithoutSync(seat);
                        break;
                    case "block":
                        screen.bookWithBlock(seat);
                        break;
                    default:
                        screen.book(seat);
                }
            });
            users[i].start();
        }
        for (Thread user : users) {
            user.join();
        }
        System.out.println(
            version + " version - seats sold: "
                + screen.getBookingsMade()
        );
    }
    public static void main(String[] args) throws InterruptedException {
        runTest("unsafe");
        runTest("synchronized method");
        runTest("block");
    }
}