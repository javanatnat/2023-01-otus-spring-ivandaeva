package ru.otus.spring.service.worktest;

import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.service.data.IOContextWorker;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestPrinterTest {
    @Test
    void testPrintTest() {
        StudentTestDao testDao = mock(StudentTestDao.class);
        when(testDao.getStudentTest()).thenReturn(new StudentTest(
                List.of(new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")))));

        IOContextWorker ioContextWorker = mock(IOContextWorker.class);
        when(ioContextWorker.getNextLine()).thenReturn("Test");
        StringBuilder buffer = new StringBuilder();
        doAnswer((invocationOnMock) -> {
            buffer.append((String) invocationOnMock.getArgument(0));
            return null;
        }).when(ioContextWorker).outputLine(anyString());
        doNothing().when(ioContextWorker).outputLine();

        TestPrinter printer = new TestPrinterImpl(testDao, ioContextWorker);
        printer.printTest();
        assertThat(buffer.toString())
                .isNotBlank()
                .startsWith("Test:");
        verify(ioContextWorker,times(4)).outputLine(anyString());
        verify(ioContextWorker,times(1)).outputLine();
    }
}
