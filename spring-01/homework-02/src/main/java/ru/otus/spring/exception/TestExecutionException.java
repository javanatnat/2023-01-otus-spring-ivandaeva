package ru.otus.spring.exception;

public class TestExecutionException extends RuntimeException {
    public TestExecutionException(Throwable cause) {
        super("Error while run student test!", cause);
    }
}
