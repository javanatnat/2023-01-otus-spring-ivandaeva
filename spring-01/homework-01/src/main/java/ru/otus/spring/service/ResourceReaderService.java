package ru.otus.spring.service;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceReaderService {
    InputStream getDataFromResource() throws IOException;
}
