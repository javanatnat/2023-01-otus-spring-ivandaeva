package ru.otus.spring.domain;

public class StudentExercise {
    private final Student student;
    private final StudentTestResult tests;

    public StudentExercise(Student student, StudentTestResult tests) {
        this.student = student;
        this.tests = tests;
    }

    public Student getStudent() {
        return student;
    }

    public StudentTestResult getTests() {
        return tests;
    }

    @Override
    public String toString() {
        return "StudentExercise{" +
                "student=" + student +
                ", tests=" + tests +
                '}';
    }
}
