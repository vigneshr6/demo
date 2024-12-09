package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CamsPCWaitNotify {
    private final Queue<Integer> queue;
    public static int MAX_QUEUE_SIZE = 5;

    public CamsPCWaitNotify() {
        queue = new LinkedList<>();
    }

    public CamsPCWaitNotify(Queue<Integer> queue) {
        this.queue = queue;
    }

    void produce() throws InterruptedException {
        Random random = new Random();
        while (true) {
            synchronized (this) {
                if (queue.size() == MAX_QUEUE_SIZE) {
                    wait();
                }
                int val = random.nextInt(1000);
                queue.add(val);
                System.out.println("Produced : " + val);
                if (queue.size() == 1) {
                    notify();
                }
            }
            Thread.sleep(200);
        }
    }

    void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (queue.isEmpty()) {
                    wait();
                }
                Integer val = queue.poll();
                System.out.println("Consumed : " + val);
                if (queue.size() == 4) {
                    notify();
                }
            }
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) {
        CamsPCWaitNotify pc = new CamsPCWaitNotify();
        Thread producer = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumer.start();
    }

}