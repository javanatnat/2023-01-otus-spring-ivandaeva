package ru.otus.spring.service.data;

import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.config.AppProps;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReaderLocaleImpl implements ResourceReader {
    private static final String TEST_FILE_NAME = "test.simple.name";
    private final MessageSource messageSource;
    private final AppProps appProps;
    private final String fileName;

    public ResourceReaderLocaleImpl(MessageSource messageSource, AppProps appProps) {
        this.messageSource = messageSource;
        this.appProps = appProps;
        this.fileName = getFileName();
    }

    @Override
    public InputStream getDataFromResource() throws IOException {
        return new ClassPathResource(fileName).getInputStream();
    }

    private String getFileName() {
        return messageSource.getMessage(TEST_FILE_NAME, null, appProps.getLocale());
    }
}
