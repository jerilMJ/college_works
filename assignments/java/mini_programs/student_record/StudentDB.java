package mini_programs.student_record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StudentDB {
    StudentDB() {
        this.db = new HashMap<Integer, Student>();
    }

    private Map<Integer, Student> db;

    public Map<Integer, Student> getDBReadOnly() {
        return Map.copyOf(db);
    }

    public void addNewStudent(Student student) throws DuplicateRecordException {
        if (db.containsKey(student.rollNumber))
            throw new DuplicateRecordException();
        else
            db.put(student.rollNumber, student);
    }

    public Student getDetailsOfStudent(int rollNumber) throws NoSuchRecordException {
        if (db.containsKey(rollNumber))
            return db.get(rollNumber);
        else
            throw new NoSuchRecordException();
    }

    public ArrayList<Student> getAllRecords() throws DBEmptyException {
        if (db.isEmpty())
            throw new DBEmptyException();
        else {
            Map<Integer, Student> students = getDBReadOnly();
            ArrayList<Student> studs = new ArrayList<Student>(students.values());
            studs.sort(new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return Integer.valueOf(s1.rollNumber).compareTo(Integer.valueOf(s2.rollNumber));
                }
            });

            return studs;
        }
    }

    public void deleteStudentRecord(int rollNumber) throws NoSuchRecordException {
        if (db.containsKey(rollNumber))
            db.remove(rollNumber);
        else
            throw new NoSuchRecordException();
    }

    public ArrayList<Student> searchByName(String name) {
        Map<Integer, Student> clonedDb = getDBReadOnly();

        Collection<Student> students = new ArrayList<Student>(clonedDb.values());
        students.removeIf(student -> !student.name.contains(name));

        ArrayList<Student> filtered = new ArrayList<Student>(students);
        filtered.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.valueOf(s1.rollNumber).compareTo(Integer.valueOf(s2.rollNumber));
            }
        });

        return filtered;
    }
}