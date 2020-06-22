package mini_programs.student_record;

import java.io.IOException;
import java.util.Scanner;

public class StudentRecord {
    public static void main(String[] args) {
        StudentDB db = new StudentDB();
        Scanner scanner = new Scanner(System.in);
        UIMenu menu = new UIMenu(scanner);

        try {
            while (true) {
                menu.showMenu();
                int option = scanner.nextInt();
                scanner.nextLine();

                if (!menu.handleInput(option, db))
                    break;

                System.in.read();
            }
        } catch (IOException ioe) {
            Logger.println("Unexpected error occurred.");
        }

        scanner.close();
    }
}
