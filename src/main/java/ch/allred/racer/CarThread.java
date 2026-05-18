package ch.allred.racer;

public class CarThread extends Thread {

    private final Car car;

    public CarThread(Car car) {
        this.car = car;
    }

    @Override
    public void run() {

        long lastTime = System.currentTimeMillis();

        while (true) {

            long now = System.currentTimeMillis();
            long diff = now - lastTime;

            lastTime = now;

            car.update((double) diff / 1000);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}