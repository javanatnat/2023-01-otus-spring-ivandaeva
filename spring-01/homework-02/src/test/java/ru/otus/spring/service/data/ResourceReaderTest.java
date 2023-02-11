package ru.otus.spring.service.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceReaderTest {
    @Test
    void testGetData() throws IOException {
        ResourceReader readerService = new ResourceReaderImpl("/test.csv");
        InputStream inputStream = readerService.getDataFromResource();
        byte[] data = inputStream.readAllBytes();
        assertThat(new String(data)).isEqualTo("test\n");
    }
}
