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
@ActiveProfiles("locale-other-lang")
public class ResourceReaderLocaleOtherTest {
    @Autowired
    @Qualifier("resourceReaderLocale")
    private ResourceReader reader;

    @Autowired
    private AppProps appProps;

    @Test
    void testAppProps() {
        assertThat(appProps.getLocale()).isEqualTo(new Locale("other"));
    }

    @Test
    void testReader() throws IOException {
        InputStream inputStream = reader.getDataFromResource();
        byte[] data = inputStream.readAllBytes();
        String testView = new String(data);

        if (Locale.getDefault().equals(new Locale("ru", "RU"))) {
            assertThat(testView).contains("Год выпуска ОС Windows 98");
        } else if (Locale.getDefault().equals(Locale.ENGLISH)) {
            assertThat(testView).contains("Year of birth OS Windows 98");
        }
    }
}
