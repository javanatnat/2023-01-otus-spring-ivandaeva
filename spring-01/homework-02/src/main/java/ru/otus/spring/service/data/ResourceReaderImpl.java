package ru.otus.spring.service.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@PropertySource("application.properties")
@Component
public class ResourceReaderImpl implements ResourceReader {
    private final String fileName;

    public ResourceReaderImpl(@Value("${test.simple.name}") String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }
}
