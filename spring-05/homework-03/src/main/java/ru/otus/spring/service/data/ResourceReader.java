package ru.otus.spring.service.data;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceReader {
    InputStream getDataFromResource() throws IOException;
}
