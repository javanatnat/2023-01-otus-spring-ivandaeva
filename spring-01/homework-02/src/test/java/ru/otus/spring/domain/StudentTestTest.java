package ru.otus.spring.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StudentTestTest {
    @Test
    void testUnmodifiable() {
        StudentTest test = new StudentTest(Collections.emptyList());
        assertThat(test.getQuestions()).isEmpty();
        assertThatThrownBy(() -> test.getQuestions().clear());
    }
}
