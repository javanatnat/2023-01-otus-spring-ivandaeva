package ru.otus.spring.domain;

import java.util.Collections;
import java.util.List;

public class StudentTestQuestion {
    private final String question;
    private final List<String> availableAnswers;
    private final List<String> rightAnswers;

    public StudentTestQuestion(
            String question,
            List<String> availableAnswers,
            List<String> rightAnswers
    ) {
        this.question = question;
        this.availableAnswers = availableAnswers;
        this.rightAnswers = rightAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAvailableAnswers() {
        return Collections.unmodifiableList(availableAnswers);
    }

    public List<String> getRightAnswers() {
        return Collections.unmodifiableList(rightAnswers);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31*result + availableAnswers.hashCode();
        result = 31*result + rightAnswers.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (! (obj instanceof StudentTestQuestion)) {
            return false;
        }

        StudentTestQuestion studentTestQuestion = (StudentTestQuestion) obj;

        return this.question.equals(studentTestQuestion.getQuestion())
                && this.availableAnswers.equals(studentTestQuestion.availableAnswers)
                && this.rightAnswers.equals(studentTestQuestion.rightAnswers);
    }

    @Override
    public String toString() {
        return "StudentTestQuestion{question=" + question
                + ", availableAnswers=" + availableAnswers
                + ", rightAnswers=" + rightAnswers + "}";
    }
}
