package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
public class AppProps {
    private Locale locale;
    private String defaultTestFileName;
    private Map<Locale, String> fileNamesByLocale;
    public Locale getLocale() {
        return locale;
    }

    public String getDefaultTestFileName() {
        return defaultTestFileName;
    }

    public String getTestFileName() {
        if (fileNamesByLocale.containsKey(locale)) {
            return fileNamesByLocale.get(locale);
        }
        return defaultTestFileName;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setDefaultTestFileName(String defaultTestFileName) {
        this.defaultTestFileName = defaultTestFileName;
    }

    public void setFileNamesByLocale(Map<Locale, String> fileNamesByLocale) {
        this.fileNamesByLocale = fileNamesByLocale;
    }
}
