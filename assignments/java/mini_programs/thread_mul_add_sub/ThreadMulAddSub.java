package mini_programs.thread_mul_add_sub;

public class ThreadMulAddSub {
    public static void main(String[] args) {
        Thread[] threads = { new MulThread("Multiplication thread", 5, 6), new AddThread("Addition Thread", 15, 20),
                new SubThread("Subtraction Thread", 9, 4) };

        try {
            for (Thread thread : threads) {
                thread.start();
                thread.join();
            }
        } catch (InterruptedException ie) {
            System.out.println("A thread was interrupted while executing.");
        }
    }
}

class OprThread extends Thread {
    OprThread(String name) {
        this.name = name;
    }

    String name;

    void whoIsTalking() {
        System.out.print(name + " is talking: ");
    }
}

class MulThread extends OprThread {
    MulThread(String name, int opr1, int opr2) {
        super(name);
        this.opr1 = opr1;
        this.opr2 = opr2;
    }

    int opr1;
    int opr2;

    @Override
    public void run() {
        super.run();
        whoIsTalking();
        System.out.println(opr1 + " x " + opr2 + " is " + (opr1 * opr2));
        System.out.println();
    }
}

class AddThread extends OprThread {
    AddThread(String name, int opr1, int opr2) {
        super(name);
        this.opr1 = opr1;
        this.opr2 = opr2;
    }

    int opr1;
    int opr2;

    @Override
    public void run() {
        super.run();
        whoIsTalking();
        System.out.println(opr1 + " + " + opr2 + " is " + (opr1 + opr2));
        System.out.println();
    }
}

class SubThread extends OprThread {
    SubThread(String name, int opr1, int opr2) {
        super(name);
        this.opr1 = opr1;
        this.opr2 = opr2;
    }

    int opr1;
    int opr2;

    @Override
    public void run() {
        super.run();
        whoIsTalking();
        System.out.println(opr1 + " - " + opr2 + " is " + (opr1 - opr2));
        System.out.println();
    }
}
