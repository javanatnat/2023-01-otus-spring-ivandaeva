package ru.otus.spring.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceReaderServiceTest {
    @Test
    void testGetData() throws IOException {
        ResourceReaderService readerService = new ResourceReaderServiceImpl("/test.csv");
        InputStream inputStream = readerService.getDataFromResource();
        byte[] data = inputStream.readAllBytes();
        assertThat(new String(data)).isEqualTo("test\n");
    }
}
