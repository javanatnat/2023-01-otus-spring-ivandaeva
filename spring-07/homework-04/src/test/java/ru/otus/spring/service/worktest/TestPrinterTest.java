package ru.otus.spring.service.worktest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.service.data.IOContextWorker;
import ru.otus.spring.service.data.TestMessageKey;
import ru.otus.spring.service.data.TestMessages;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestPrinterTest {
    @Mock
    private StudentTestDao testDao;
    @Mock
    private IOContextWorker ioContextWorker;
    @Mock
    private TestMessages testMessages;

    @Test
    void testPrintTest() {
        when(testDao.getStudentTest()).thenReturn(new StudentTest(
                List.of(new StudentTestQuestion(
                        "2+2",
                        List.of("3", "4", "5", "8"),
                        List.of("4")))));

        StringBuilder buffer = new StringBuilder();
        doAnswer((invocationOnMock) -> {
            buffer.append((String) invocationOnMock.getArgument(0));
            return null;
        }).when(ioContextWorker).outputLine(anyString());
        doNothing().when(ioContextWorker).outputLine();
        when(testMessages.getMessage(any(TestMessageKey.class))).thenReturn("TEST");

        TestPrinter printer = new TestPrinterImpl(testDao, ioContextWorker, testMessages);
        printer.printTest();
        assertThat(buffer.toString())
                .isNotBlank()
                .startsWith("TEST:");
        verify(ioContextWorker,times(4)).outputLine(anyString());
        verify(ioContextWorker,times(1)).outputLine();
    }
}
