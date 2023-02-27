package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.service.data.ResourceReader;
import ru.otus.spring.service.data.ResourceTestParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentTestDaoTest {
    @Mock
    private ResourceReader reader;
    @Mock
    private ResourceTestParser testParser;

    @Test
    void testDaoMock() throws IOException {
        when(reader.getDataFromResource()).thenReturn(InputStream.nullInputStream());

        when(testParser.parseTest(any(InputStream.class))).thenReturn(new StudentTest(
                List.of(new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")))
        ));

        StudentTestDao testDao = new StudentTestDaoImpl(reader, testParser);
        StudentTest test = testDao.getStudentTest();

        assertThat(test.getQuestions()).containsExactly(
                new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")));
    }
}
