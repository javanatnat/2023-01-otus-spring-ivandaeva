package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.dao.StudentTestDaoImpl;
import ru.otus.spring.service.data.ResourceReader;
import ru.otus.spring.service.data.ResourceTestParser;

@Configuration
public class DaoConfig {
    @Bean
    public StudentTestDao studentTestDao(
            ResourceReader resourceReader,
            ResourceTestParser resourceTestParser) {
        return new StudentTestDaoImpl(resourceReader, resourceTestParser);
    }
}
