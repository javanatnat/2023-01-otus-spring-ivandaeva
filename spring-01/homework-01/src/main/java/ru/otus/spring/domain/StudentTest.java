package ru.otus.spring.domain;

import java.util.Collections;
import java.util.List;

public class StudentTest {
    private final List<StudentTestQuestion> questions;

    public StudentTest(List<StudentTestQuestion> questions) {
        this.questions = questions;
    }

    public List<StudentTestQuestion> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    @Override
    public int hashCode() {
        return questions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (! (obj instanceof StudentTest)) {
            return false;
        }

        StudentTest studentTest = (StudentTest) obj;

        return studentTest.getQuestions().equals(questions);
    }

    @Override
    public String toString() {
        return "StudentTest{questions: " + questions + "}";
    }
}
