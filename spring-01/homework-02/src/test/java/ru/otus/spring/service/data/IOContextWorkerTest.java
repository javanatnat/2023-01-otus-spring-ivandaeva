package ru.otus.spring.service.data;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class IOContextWorkerTest {
    @Test
    void testGetNextLine() {
        IOContext ioContext = mock(IOContext.class);
        when(ioContext.getInputStream()).thenReturn(new ByteArrayInputStream("test\n".getBytes(StandardCharsets.UTF_8)));
        IOContextWorker ioContextWorker = new IOContextWorkerImpl(ioContext);
        assertThat(ioContextWorker.getNextLine()).isEqualTo("test");
    }

    @Test
    void testOutputLine() {
        IOContext ioContext = mock(IOContext.class);
        PrintStream printStream = mock(PrintStream.class);

        doNothing().when(printStream).println(anyString());
        when(ioContext.getPrintStream()).thenReturn(printStream);
        when(ioContext.getInputStream()).thenReturn(InputStream.nullInputStream());

        IOContextWorker ioContextWorker = new IOContextWorkerImpl(ioContext);
        ioContextWorker.outputLine("test");
        verify(printStream, times(1)).println("test");
    }
}
