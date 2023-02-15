package ru.otus.spring.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudentTestResult {
    private final StudentTest test;
    private final Map<String, List<String>> results;

    public StudentTestResult(StudentTest test) {
        this(test, new HashMap<>());
    }

    public StudentTestResult(
            StudentTest test,
            Map<String, List<String>> results
    ) {
        Objects.requireNonNull(test);
        this.test = test;
        this.results = results;
    }

    public StudentTest getTest() {
        return test;
    }

    public Map<String, List<String>> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "StudentTestResult{" +
                "test=" + test +
                ", results=" + results +
                '}';
    }
}
