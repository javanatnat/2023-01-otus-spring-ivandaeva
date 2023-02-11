package ru.otus.spring.service.data;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class IOContextWorkerImpl implements IOContextWorker {
    private final IOContext ioContext;
    private final Scanner scanner;

    public IOContextWorkerImpl(IOContext ioContext) {
        this.ioContext = ioContext;
        scanner = new Scanner(ioContext.getInputStream());
    }


    @Override
    public String getNextLine() {
        String input = "";
        do {
            if (scanner.hasNext()) {
                input = scanner.nextLine();
            }
        } while (input.trim().isBlank());
        return input;
    }

    @Override
    public void outputLine(String str) {
        ioContext.getPrintStream().println(str);
    }

    @Override
    public void outputLine() {
        ioContext.getPrintStream().println();
    }
}
