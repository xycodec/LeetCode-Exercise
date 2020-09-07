package com.xycode.leetcode;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

/**
 * ClassName: leetcode1116
 *
 * @Author: xycode
 * @Date: 2019/12/3
 * @Description: this is description of the leetcode1116 class
 **/
public class LeetCodeConcurrency {
    //leetcode, Concurrency部分

    //1114. Print in Order
    public class Foo {
        CountDownLatch firstSecond = new CountDownLatch(1);
        CountDownLatch secondThree = new CountDownLatch(1);

        public Foo() {

        }

        public void first(Runnable printFirst) throws InterruptedException {

            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            firstSecond.countDown();
        }

        public void second(Runnable printSecond) throws InterruptedException {

            // printSecond.run() outputs "second". Do not change or remove this line.
            firstSecond.await();
            printSecond.run();
            secondThree.countDown();
        }

        public void third(Runnable printThird) throws InterruptedException {
            firstSecond.await();
            secondThree.await();
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        }
    }

    //1115. Print FooBar Alternately
    class FooBar {
        private int n;
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);

        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {

                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                countDownLatch1.countDown();
                countDownLatch2.await();
                countDownLatch2 = new CountDownLatch(1);
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                countDownLatch1.await();
                countDownLatch1 = new CountDownLatch(1);
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                countDownLatch2.countDown();
            }
        }
    }

    //1116 Print Zero Even Odd
    class ZeroEvenOdd {
        private int n;
        private Queue<Integer> queue;
        private Object o = new Object();

        public ZeroEvenOdd(int n) {
            this.n = n;
            queue = new LinkedList<>();
            for (int i = 1; i <= 2 * n; ++i) {
                if (i % 2 == 1) {
                    queue.add(0);
                } else {
                    queue.add(i / 2);
                }
            }
//            while(!queue.isEmpty()){
//                System.out.print(queue.poll());
//            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public synchronized void zero(IntConsumer printNumber) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() == 0) {
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }

        public void even(IntConsumer printNumber) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() != 0 && queue.peek() % 2 == 0) {
                        printNumber.accept(queue.poll());

                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }

        public void odd(IntConsumer printNumber) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() % 2 == 1) {
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }
    }

    //1117 Building H2O
    class H2O {
        private Semaphore h = new Semaphore(2);
        private Semaphore o = new Semaphore(1);
        private AtomicInteger count = new AtomicInteger(0);

        public H2O() {

        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            h.acquire();
            count.incrementAndGet();
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            if (count.get() == 2) {
                count.addAndGet(-2);
                o.release();
            }

        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            o.acquire();
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            h.release(2);
        }
    }

    //1195 Fizz Buzz Multithreaded
    class FizzBuzz {
        private int n;
        private Queue<Integer> queue;
        private Object o = new Object();

        public FizzBuzz(int n) {
            this.n = n;
            this.queue = new LinkedList<>();
            for (int i = 1; i <= n; ++i) {
                this.queue.add(i);
            }
        }

        // printFizz.run() outputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() % 3 == 0 && queue.peek() % 5 != 0) {
                        printFizz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() % 5 == 0 && queue.peek() % 3 != 0) {
                        printBuzz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() % 5 == 0 && queue.peek() % 3 != 0) {
                        printFizzBuzz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber) throws InterruptedException {
            while (!queue.isEmpty()) {
                synchronized (o) {
                    while (!queue.isEmpty() && queue.peek() % 5 != 0 && queue.peek() % 3 != 0) {
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }
    }

    //1226. The Dining Philosophers
    class DiningPhilosophers {
        private Queue<Integer> queue;
        private Object o;

        public DiningPhilosophers() {
            queue = new ArrayDeque<>(300);
            o = new Object();
            List<Integer> tmp = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
            for (int i = 0; i < 60; ++i) {
                Collections.shuffle(tmp);
                queue.addAll(tmp);
            }
        }

        // call the run() method of any runnable to execute its code
        public void wantsToEat(int philosopher,
                               Runnable pickLeftFork,
                               Runnable pickRightFork,
                               Runnable eat,
                               Runnable putLeftFork,
                               Runnable putRightFork) throws InterruptedException {
            while (true) {
                synchronized (o) {
                    if (philosopher == queue.peek()) {
                        pickLeftFork.run();
                        pickRightFork.run();
                        eat.run();
                        putLeftFork.run();
                        putRightFork.run();
                        queue.poll();
                        break;
                    }
                }
                TimeUnit.MICROSECONDS.sleep(10);//Questioner's bug
            }
        }
    }


}
