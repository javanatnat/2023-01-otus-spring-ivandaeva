package ru.otus.spring.service.data;

import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.config.AppProps;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReaderLocaleImpl implements ResourceReader {
    private final String fileName;

    public ResourceReaderLocaleImpl(AppProps appProps) {
        this.fileName = appProps.getTestFileName();
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }
}
