package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CamsPCBlockingQueue {

    public static int MAX_QUEUE_SIZE = 5;
    //LinkedBlockingQueue has separate locks for consumers and producers
    private final BlockingQueue<Integer> queue;

    public CamsPCBlockingQueue() {
        queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    }

    public CamsPCBlockingQueue(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    void produce(int prodId) throws InterruptedException {
        Random random = new Random();
        while (true) {
            int val = random.nextInt(1000);
            //waits till space is available
            queue.put(val);
            System.out.printf("ProducerId :: %d \t\tProduced : %s\n", prodId, val);
            Thread.sleep(200);
        }
    }

    void consume(int conId) throws InterruptedException {
        while (true) {
            //waits till data is available
            Integer val = queue.take();
            System.out.printf("ConsumerId :: %d \t\tConsumed : %s\n", conId, val);
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) {
        CamsPCBlockingQueue pc = new CamsPCBlockingQueue(new LinkedBlockingQueue<>(MAX_QUEUE_SIZE));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    pc.produce(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            executorService.submit(() -> {
                try {
                    pc.consume(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
}
