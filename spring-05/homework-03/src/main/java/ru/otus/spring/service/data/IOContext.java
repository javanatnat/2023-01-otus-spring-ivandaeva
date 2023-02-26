package ru.otus.spring.service.data;

import java.io.InputStream;
import java.io.PrintStream;

public interface IOContext {
    PrintStream getPrintStream();
    InputStream getInputStream();
}
