package ru.otus.spring.service.worktest;

import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;
import ru.otus.spring.service.data.IOContextWorker;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestRunnerTest {
    @Test
    void testPrintTest() {
        StudentTestDao testDao = mock(StudentTestDao.class);
        when(testDao.getStudentTest()).thenReturn(new StudentTest(
                List.of(new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")))));

        IOContextWorker ioContextWorker = mock(IOContextWorker.class);
        IOContextWorker spyIOContextWorker = spy(ioContextWorker);
        when(spyIOContextWorker.getNextLine()).thenReturn("Test");
        StringBuilder buffer = new StringBuilder();
        doAnswer((invocationOnMock) -> {
            buffer.append((String) invocationOnMock.getArgument(0));
            return null;
        }).when(spyIOContextWorker).outputLine(anyString());
        doNothing().when(spyIOContextWorker).outputLine();

        TestChecker testChecker = mock(TestChecker.class);
        when(testChecker.testIsPassed(any(StudentTestResult.class))).thenReturn(true);

        TestRunner runner = new TestRunnerImpl(testDao, testChecker, spyIOContextWorker);
        runner.runTest();
        assertThat(buffer.toString())
                .isNotBlank()
                .startsWith("Введите ваши данные")
                .contains("2+2")
                .contains("Test")
                .endsWith("Тестирование окончено!");
        verify(spyIOContextWorker, atLeastOnce()).outputLine(anyString());
        verify(spyIOContextWorker, atLeastOnce()).outputLine();
        verify(spyIOContextWorker, atLeastOnce()).getNextLine();
        verify(testChecker,times(1)).testIsPassed(any(StudentTestResult.class));
    }
}
