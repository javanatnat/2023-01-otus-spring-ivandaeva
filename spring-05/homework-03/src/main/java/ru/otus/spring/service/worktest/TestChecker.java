package ru.otus.spring.service.worktest;

import ru.otus.spring.domain.StudentTestResult;

public interface TestChecker {
    boolean testIsPassed(StudentTestResult studentTestResult);
}
