package calculator;

public class Logger {
    public static void log(Object msg) {
        System.out.println(msg);
    }

    public static void logSpaced(Object msg) {
        log("\n");
        log(msg);
    }

    public static void error(Object msg) {
        log("-------------------------------------E-R-R-O-R-------------------------------------");
        log("");
        log(msg);
        log("");
        log("-----------------------------------------------------------------------------------");
    }
}