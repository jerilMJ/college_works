package mini_programs.student_record;

public class Student {
    Student(int rollNumber, String name) {
        this.rollNumber = rollNumber;
        this.name = name;
    }

    int rollNumber;
    String name;

    @Override
    public String toString() {
        return rollNumber + ": " + name;
    }
}