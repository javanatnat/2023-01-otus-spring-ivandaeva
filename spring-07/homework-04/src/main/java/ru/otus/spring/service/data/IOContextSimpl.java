package ru.otus.spring.service.data;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;

@Component
public class IOContextSimpl implements IOContext {
    @Override
    public PrintStream getPrintStream() {
        return System.out;
    }

    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
