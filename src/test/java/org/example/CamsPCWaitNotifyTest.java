package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CamsPCWaitNotifyTest {

    private Queue<Integer> queue;
    private CamsPCWaitNotify waitNotify;

    @BeforeEach
    void setUp() {
        queue = new LinkedList<>();
        waitNotify = new CamsPCWaitNotify(queue);
    }

    @Test
    @Timeout(5000)
    void testProduce() throws InterruptedException {
        new Thread(() -> {
            try {
                waitNotify.produce();
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
                waitNotify.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //Wait till queue is empty
        Thread.sleep(2600);
        assertTrue(queue.isEmpty());

    }
}