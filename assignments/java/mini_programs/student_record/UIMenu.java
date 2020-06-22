package mini_programs.student_record;

import java.util.ArrayList;
import java.util.Scanner;

public class UIMenu {
    UIMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    Scanner scanner;

    public void showMenu() {
        Logger.println("-----------------------------------------------------------------");
        Logger.println("|                    STUDENT RECORD MANAGER                     |");
        Logger.println("|                                                               |");
        Logger.println("|     1. Add new student                                        |");
        Logger.println("|     2. View all records                                       |");
        Logger.println("|     3. Search by roll number                                  |");
        Logger.println("|     4. Search by name                                         |");
        Logger.println("|     5. Delete a record                                        |");
        Logger.println("|     6. Exit application                                       |");
        Logger.println("|                                                               |");
        Logger.println("-----------------------------------------------------------------");
    }

    public boolean handleInput(int option, StudentDB db) {
        boolean continueApp = true;

        try {
            switch (option) {
                case 1:
                    addStud(db);
                    break;
                case 2:
                    viewAll(db);
                    break;
                case 3:
                    searchRoll(db);
                    break;
                case 4:
                    searchName(db);
                    break;
                case 5:
                    deleteStud(db);
                    break;
                case 6:
                    continueApp = false;
                    break;
                default:
            }
        } catch (DuplicateRecordException dre) {
            Logger.println("There is a record in the db with that roll number already.");
        } catch (NoSuchRecordException nsre) {
            Logger.println("There is no such record in the db.");
        } catch (DBEmptyException dbee) {
            Logger.println("The db is empty.");
        }

        return continueApp;
    }

    private void deleteStud(StudentDB db) throws NoSuchRecordException {
        int rollNumber;
        Logger.println("Enter roll number of record to delete: ");
        rollNumber = scanner.nextInt();

        scanner.nextLine();

        db.deleteStudentRecord(rollNumber);
        Logger.print("Deleted!");
    }

    private void searchName(StudentDB db) {
        String name;
        ArrayList<Student> students;
        Logger.print("Enter part of name to search: ");
        name = scanner.nextLine();
        students = db.searchByName(name);

        Logger.println("Found " + students.size() + " record" + ((students.size() > 1) ? "s:" : ":"));

        students.forEach(stud -> Logger.println(stud));
    }

    private void searchRoll(StudentDB db) throws NoSuchRecordException {
        int rollNumber;
        Logger.print("Enter the roll number to search: ");
        rollNumber = scanner.nextInt();

        scanner.nextLine();

        Student student = db.getDetailsOfStudent(rollNumber);
        Logger.println("Found!");
        Logger.println(student);
    }

    private void viewAll(StudentDB db) throws DBEmptyException {
        ArrayList<Student> students;
        students = db.getAllRecords();
        Logger.println("Showing all student records");

        students.forEach(student -> Logger.println(student));
    }

    private void addStud(StudentDB db) throws DuplicateRecordException {
        int rollNumber;
        String name;
        Logger.print("Enter the roll number: ");
        rollNumber = scanner.nextInt();

        scanner.nextLine();

        Logger.print("Enter the name of student: ");
        name = scanner.nextLine();

        db.addNewStudent(new Student(rollNumber, name));
        Logger.println("Added!");
    }
}