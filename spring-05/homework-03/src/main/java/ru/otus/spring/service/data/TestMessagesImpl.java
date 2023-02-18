package ru.otus.spring.service.data;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class TestMessagesImpl implements TestMessages{
    private final MessageSource messageSource;
    private final Locale locale;

    public TestMessagesImpl(
            MessageSource messageSource,
            Locale locale
    ) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public String getMessage(TestMessageKey key) {
        return getTestMessage(key.getKeyName());
    }

    private String getTestMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }
}
