package ru.otus.spring.exception;

public class LibraryDBException extends RuntimeException {
    public LibraryDBException(String message) {
        super(message);
    }

    public LibraryDBException(Throwable cause) {
        super(cause);
    }

    public LibraryDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
