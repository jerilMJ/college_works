package mini_programs.student_record;

@SuppressWarnings("serial")
class StudentDBException extends Exception {
}

@SuppressWarnings("serial")
class DBEmptyException extends StudentDBException {
}

@SuppressWarnings("serial")
class DuplicateRecordException extends StudentDBException {
}

@SuppressWarnings("serial")
class NoSuchRecordException extends StudentDBException {
}