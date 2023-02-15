package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.service.data.ResourceReader;
import ru.otus.spring.service.data.ResourceReaderLocaleImpl;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {

    @Bean
    public ResourceReader resourceReaderLocale(MessageSource messageSource, AppProps appProps) {
        return new ResourceReaderLocaleImpl(messageSource, appProps);
    }
}
