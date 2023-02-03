package ru.otus.spring.service.data;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReaderImpl implements ResourceReader {
    private final String fileName;

    public ResourceReaderImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }
}
