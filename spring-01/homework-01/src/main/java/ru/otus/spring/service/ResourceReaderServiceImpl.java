package ru.otus.spring.service;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReaderServiceImpl implements ResourceReaderService{
    private final String fileName;

    public ResourceReaderServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }
}
