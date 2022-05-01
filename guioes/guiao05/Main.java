class Barreira {
    private final int N;
    private int counter = 0;
    
    Barreira(int N) {
        this.N = N;
    }
    
    public int getCounter() {
        return this.counter;
    }

    public synchronized void await() throws InterruptedException {
        int c = ++this.counter;

        while (this.counter < this.N)
            wait();

        if (this.counter == this.N)
            notifyAll();
        else if (c == 1)
            this.counter = 0;
    }

    // public synchronized void await() throws InterruptedException {
    //     int c = ++this.counter;
    //     if (this.counter == this.N)
    //         notifyAll();
    //     else
    //         while (this.counter < this.N)
    //             wait();
        
    //     if (c == 1)
    //         this.counter = 0;
        
    // }
}

public class Main {
    public static void main(String args[]) {
        try {
            int N = 5;
            Barreira b = new Barreira(N);

            for (int i = 0; i < N; i++) {
                new Thread(() -> {
                    try {
                        System.out.println("Await");
                        b.await();
                        System.out.println("Released");
                    } catch (Exception e) {}
                }).start();
                Thread.sleep(200);
            }

            System.out.println(b.getCounter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}