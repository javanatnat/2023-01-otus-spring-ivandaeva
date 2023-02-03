package ru.otus.spring.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StudentTestQuestionTest {
    @Test
    void testEquals() {
        StudentTestQuestion question = new StudentTestQuestion("A?", List.of("A", "F", "G"), List.of("F"));
        StudentTestQuestion sameQuestion = new StudentTestQuestion("A?", List.of("A", "F", "G"), List.of("F"));
        StudentTestQuestion notSameQuestion = new StudentTestQuestion("A?", List.of("A", "f", "G"), List.of("F"));

        assertThat(question).isEqualTo(sameQuestion);
        assertThat(question.hashCode()).isEqualTo(sameQuestion.hashCode());

        assertThat(question).isNotEqualTo(notSameQuestion);
        assertThat(question.hashCode()).isNotEqualTo(notSameQuestion.hashCode());
    }

    @Test
    void testUnmodifiable() {
        StudentTestQuestion question = new StudentTestQuestion("A?", List.of("A", "F", "G"), List.of("F"));

        assertThatThrownBy(() -> question.getAvailableAnswers().clear());
        assertThatThrownBy(() -> question.getRightAnswers().clear());
    }
}
