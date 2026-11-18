package Assignment3;

class ScanTask implements Runnable {
    private final int station;
    private final int scripts;
    ScanTask(int station, int scripts) {
        this.station = station;
        this.scripts = scripts;
    }
    @Override
    public void run() {
        System.out.println("Station " + station + " started");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        System.out.println(
            "Station " + station + " scanned " + scripts + " scripts"
        );
    }
}
class ScanThread extends Thread {
    private final ScanTask task;
    ScanThread(int station, int scripts) {
        task = new ScanTask(station, scripts);
    }
    @Override
    public void run() {
        task.run();
    }
}
public class ScannerStations {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new ScanThread(1, 10),
            new ScanThread(2, 20),
            new Thread(new ScanTask(3, 30)),
            new Thread(new ScanTask(4, 40))
        };
        for (Thread station : stations) {
            station.start();
        }
        System.out.println(
            "Station 1 Alive: " + stations[0].isAlive()
        );
        for (Thread station : stations) {
            station.join();
        }
        System.out.println("All stations completed");
        System.out.println("Total scripts scanned: 100");
    }
}
