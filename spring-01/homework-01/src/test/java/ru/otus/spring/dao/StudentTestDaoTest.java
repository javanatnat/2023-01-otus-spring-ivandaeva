package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.service.ResourceReaderService;
import ru.otus.spring.service.ResourceReaderServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentTestDaoTest {
    @Test
    void testDao() {
        ResourceReaderService readerService = new ResourceReaderServiceImpl("StudentTest.csv");
        StudentTestDao testDao = new StudentTestDaoImpl(readerService);
        StudentTest test = testDao.getStudentTest();
        assertThat(test.getQuestions()).containsExactly(
                new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")),
                new StudentTestQuestion(
                        "2*2",
                        List.of("3", "4", "5", "8"),
                        List.of("4"))
        );
    }

    @Test
    void testDaoWithErrors() {
        ResourceReaderService readerService = new ResourceReaderServiceImpl("StudentTestWithErrors.csv");
        StudentTestDao testDao = new StudentTestDaoImpl(readerService);
        StudentTest test = testDao.getStudentTest();
        assertThat(test.getQuestions()).containsExactly(
                new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")),
                new StudentTestQuestion(
                        "2*2",
                        List.of("3", "4", "5", "8"),
                        List.of("4"))
        );
    }

    @Test
    void testDaoEmptyTest() {
        ResourceReaderService readerService = new ResourceReaderServiceImpl("test.csv");
        StudentTestDao testDao = new StudentTestDaoImpl(readerService);
        StudentTest test = testDao.getStudentTest();
        assertThat(test.getQuestions()).isEmpty();
    }
}
