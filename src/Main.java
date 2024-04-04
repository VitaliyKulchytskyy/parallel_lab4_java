import java.util.concurrent.Semaphore;
import static java.lang.Thread.sleep;

public class Main {
    static final int philosophers_num = 5;

    public static class Philosopher extends Thread {
        static Semaphore Waiter = new Semaphore(1);
        static Semaphore[] Forks = {
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1)
        };

        public static int left(int id) {
            return id % philosophers_num;
        }

        public static int right(int id) {
            return (id + 1) % philosophers_num;
        }

        private final int _id;

        public Philosopher(int id) {
            _id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("P" + _id + " is thinking");
                    Thread.sleep(200);
                    System.out.println("P" + _id + " is hungry");

                    Waiter.acquire();
                    Forks[left(_id)].acquire();
                    Forks[right(_id)].acquire();
                    System.out.println("P" + _id + " is eating");
                    Thread.sleep(200);
                    Forks[left(_id)].release();
                    Forks[right(_id)].release();
                    Waiter.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("P" + _id + " is leaving");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < philosophers_num; i++) {
            new Philosopher(i).start();
        }
    }
}