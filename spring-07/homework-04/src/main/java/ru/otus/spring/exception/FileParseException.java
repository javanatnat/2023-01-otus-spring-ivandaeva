package ru.otus.spring.exception;

public class FileParseException extends RuntimeException {
    public FileParseException(Throwable cause) {
        super("Error while read file with student test!", cause);
    }
}
