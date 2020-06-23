package mini_programs.thread_multitask;

import java.util.Random;

public class ThreadMultiTask {
    public static void main(String[] args) {
        SharedClass instance = new SharedClass();
        SomeThread[] threads = { new SomeThread("Thread 1", instance), new SomeThread("Thread 2", instance),
                new SomeThread("Thread 3", instance) };

        try {
            for (Thread thread : threads)
                thread.start();

            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException ie) {
            System.out.println("A thread was interrupted while executing.");
        }

    }
}

class SharedClass {
    synchronized public void printMessage(String msg) {
        System.out.println(msg);
    }
}

class SomeThread extends Thread {
    SomeThread(String name, SharedClass instance) {
        this.name = name;
        this.instance = instance;
    }

    String name;
    SharedClass instance;

    @Override
    public void run() {
        super.run();
        String[] greetings = { "hello", "how are you?", "good morning", "good evening", "good afternoon", "good night",
                "hope you're well", "stay home", "stay safe" };
        instance.printMessage(name + " says " + greetings[new Random().nextInt(greetings.length)]);
    }
}