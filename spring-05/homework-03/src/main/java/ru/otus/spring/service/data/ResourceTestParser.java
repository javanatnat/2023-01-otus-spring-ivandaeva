package ru.otus.spring.service.data;

import ru.otus.spring.domain.StudentTest;

import java.io.InputStream;

public interface ResourceTestParser {
    StudentTest parseTest(InputStream inputStream);
}
