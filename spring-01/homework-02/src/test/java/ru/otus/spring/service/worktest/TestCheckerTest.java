package ru.otus.spring.service.worktest;

import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCheckerTest {
    private final TestChecker checker = new TestCheckerSimple();

    @Test
    void testCheckPassedEmpty() {
        assertThat(checker.testIsPassed(new StudentTestResult(StudentTest.emptyTest()))).isFalse();
        assertThat(checker.testIsPassed(new StudentTestResult(StudentTest.emptyTest(), Collections.emptyMap())))
                .isFalse();
    }

    @Test
    void testCheckPassed() {
        StudentTest test = new StudentTest(List.of(
                new StudentTestQuestion("test", List.of("a", "g", "j", "r"), List.of("r", "a")),
                new StudentTestQuestion("test2", List.of("a", "g", "j"), List.of("j")),
                new StudentTestQuestion("test3", List.of("a", "t", "j"), List.of("t"))
        ));

        Map<String, List<String>> studentResults = Map.of("test", List.of("r"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of("test", List.of("r", "a"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of(
                "test", List.of("r", "a"),
                "test2", List.of("j"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of(
                "test", List.of("r", "a"),
                "test2", List.of("j"),
                "test3", List.of("a"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of(
                "TEST", List.of("r", "a"),
                "test2", List.of("j"),
                "test3", List.of("t"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of(
                "test", List.of("r", "A"),
                "test2", List.of("j", "j"),
                "test3", List.of("T"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isFalse();

        studentResults = Map.of(
                "test", List.of("r", "a"),
                "test2", List.of("j"),
                "test3", List.of("t"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isTrue();

        studentResults = Map.of(
                "test", List.of("r", "A"),
                "test2", List.of("j"),
                "test3", List.of("T"));
        assertThat(checker.testIsPassed(new StudentTestResult(test, studentResults))).isTrue();
    }
}
