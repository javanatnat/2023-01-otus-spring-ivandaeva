package ru.otus.spring.service.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.config.AppProps;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceReaderTest {

    @Mock
    private AppProps appProps;

    @Test
    void testGetData() throws IOException {
        when(appProps.getDefaultTestFileName()).thenReturn("/test.csv");
        ResourceReader readerService = new ResourceReaderImpl(appProps);
        InputStream inputStream = readerService.getDataFromResource();
        byte[] data = inputStream.readAllBytes();
        assertThat(new String(data)).isEqualTo("test\n");
    }
}
