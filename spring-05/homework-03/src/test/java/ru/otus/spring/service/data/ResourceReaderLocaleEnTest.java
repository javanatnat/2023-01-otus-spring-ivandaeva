package ru.otus.spring.service.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.config.AppProps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("locale-en")
public class ResourceReaderLocaleEnTest {
    @Autowired
    @Qualifier("resourceReaderLocale")
    private ResourceReader reader;

    @Autowired
    private AppProps appProps;

    @Test
    void testAppProps() {
        assertThat(appProps.getLocale()).isEqualTo(Locale.ENGLISH);
    }

    @Test
    void testReader() throws IOException {
        InputStream inputStream = reader.getDataFromResource();
        byte[] data = inputStream.readAllBytes();
        String testView = new String(data);
        assertThat(testView).contains("Year of birth OS Windows 98");
    }
}
