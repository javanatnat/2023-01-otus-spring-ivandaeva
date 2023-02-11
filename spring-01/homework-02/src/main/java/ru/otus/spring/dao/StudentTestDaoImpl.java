package ru.otus.spring.dao;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.exception.FileParseException;
import ru.otus.spring.service.data.ResourceReader;
import ru.otus.spring.service.data.ResourceTestParser;

import java.io.IOException;
import java.io.InputStream;

@Component
public class StudentTestDaoImpl implements StudentTestDao {
    private final ResourceReader resourceReader;
    private final ResourceTestParser testParser;

    public StudentTestDaoImpl(
            ResourceReader resourceReader,
            ResourceTestParser testParser
    ) {
        this.resourceReader = resourceReader;
        this.testParser = testParser;
    }

    @Override
    public StudentTest getStudentTest() {
        try (InputStream inputStream = resourceReader.getDataFromResource()){
            return testParser.parseTest(inputStream);
        } catch (IOException e) {
            throw new FileParseException(e);
        }
    }

}
