package mini_programs.student_record;

class StudentDBException extends Exception {
}

class DBEmptyException extends StudentDBException {
}

class DuplicateRecordException extends StudentDBException {
}

class NoSuchRecordException extends StudentDBException {
}