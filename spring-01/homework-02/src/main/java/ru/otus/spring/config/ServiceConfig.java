package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.service.data.*;
import ru.otus.spring.service.worktest.*;

@PropertySource("application.properties")
@Configuration
public class ServiceConfig {
    @Bean
    public ResourceReader resourceReader(@Value("${test.simple.name}") String fileName) {
        return new ResourceReaderImpl(fileName);
    }

    @Bean
    public ResourceTestParser resourceTestParser() {
        return new ResourceTestParserImpl();
    }

    @Bean
    public DataReader dataReader() {
        return new DataReaderConsole();
    }

    @Bean
    public TestChecker testChecker() {
        return new TestCheckerSimple();
    }

    @Bean
    public TestPrinter testPrinter(StudentTestDao studentTestDao) {
        return new TestPrinterImpl(studentTestDao);
    }

    @Bean
    public TestRunner testRunner(
            StudentTestDao studentTestDao,
            TestChecker testChecker,
            DataReader dataReader
    ) {
        return new TestRunnerImpl(studentTestDao, testChecker, dataReader);
    }
}
