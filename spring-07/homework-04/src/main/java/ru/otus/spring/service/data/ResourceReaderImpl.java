package ru.otus.spring.service.data;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.spring.config.AppProps;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ResourceReaderImpl implements ResourceReader {
    private final String fileName;

    public ResourceReaderImpl(AppProps appProps) {
        this.fileName = appProps.getDefaultTestFileName();
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }
}
