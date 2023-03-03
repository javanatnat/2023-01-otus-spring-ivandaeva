package ru.otus.spring.service.data;

public interface IOContextWorker {
    String getNextLine();
    void outputLine(String str);
    void outputLine();
}
