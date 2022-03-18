class Counter {
    int iterations;

    Counter() {
        this.iterations = 0;
    }

    public void increment(int step) {
        this.iterations += step;
    }

    public int getIterations() {
        return this.iterations;
    }
}

class Incrementer extends Thread {
    final int iterations;
    final Counter c;

    Incrementer(int iterations, Counter counter) {
        this.iterations = iterations;
        this.c = counter;
    }

    public void run() {
        for (int i = 0; i < iterations; i++) {
            c.increment(1);
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int N = Integer.parseInt(args[0]);
        final int I = Integer.parseInt(args[1]);
        Counter counter = new Counter();
        Thread[] arr = new Thread[N];

        for (int i = 0; i < N; i++)
            arr[i] = new Incrementer(I, counter);

        for (int i = 0; i < N; i++)
            arr[i].start();

        for (int i = 0; i < N; i++)
            arr[i].join();

        System.out.println(
            counter.getIterations()
        );
    }
}
