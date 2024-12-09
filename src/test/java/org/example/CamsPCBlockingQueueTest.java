package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CamsPCBlockingQueueTest {

    private BlockingQueue<Integer> queue;
    private CamsPCBlockingQueue pcBlockingQueue;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>(5);
        pcBlockingQueue = new CamsPCBlockingQueue(queue);
    }

    @Test
    @Timeout(5000)
    void testProduce() throws InterruptedException {
        new Thread(() -> {
            try {
                pcBlockingQueue.produce(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //Wait till queue is full
        Thread.sleep(1100);
        assertEquals(5, queue.size());

    }

    @Test
    @Timeout(5000)
    void testConsume() throws InterruptedException {
        assertTrue(queue.isEmpty());

        //add 5 elements to queue
        IntStream.range(1, 5)
                .forEach(queue::add);

        new Thread(() -> {
            try {
                pcBlockingQueue.consume(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //Wait till queue is empty
        Thread.sleep(2600);
        assertTrue(queue.isEmpty());

    }
}