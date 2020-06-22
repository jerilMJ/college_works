package mini_programs.four_threads;

import java.util.ArrayList;

class ClassToBeShared {
    ClassToBeShared() {
    }

    public synchronized void printHello(String threadName) {
        System.out.println(threadName + " says hello");
    }
}

class SomeThread extends Thread {
    ClassToBeShared instance;
    String name;

    SomeThread(ClassToBeShared instance, String name) {
        this.instance = instance;
        this.name = name;
    }

    @Override
    public void run() {
        super.run();
        instance.printHello(name);
    }
}

public class FourThreads {
    public static void main(String[] args) {
        ClassToBeShared instanceOne = new ClassToBeShared();
        ClassToBeShared instanceTwo = new ClassToBeShared();
        ArrayList<SomeThread> threads = new ArrayList<SomeThread>();

        for (int i = 0; i < 2; i++)
            threads.add(new SomeThread(instanceOne, "Thread " + (i + 1)));
        for (int i = 2; i < 4; i++)
            threads.add(new SomeThread(instanceTwo, "Thread " + (i + 1)));

        threads.forEach(thread -> thread.start());

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                System.out.println("A thread was interrupted");
            }
        });

    }
}