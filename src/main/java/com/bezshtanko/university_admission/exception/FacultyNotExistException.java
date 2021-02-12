package com.bezshtanko.university_admission.exception;

public class FacultyNotExistException extends RuntimeException {

    public FacultyNotExistException() {
        super();
    }

    public FacultyNotExistException(String message) {
        super(message);
    }

    public FacultyNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacultyNotExistException(Throwable cause) {
        super(cause);
    }

    protected FacultyNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
