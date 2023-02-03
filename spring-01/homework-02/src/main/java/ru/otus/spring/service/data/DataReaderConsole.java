package ru.otus.spring.service.data;

import java.util.Scanner;

public class DataReaderConsole implements DataReader {
    private final Scanner scanner;

    public DataReaderConsole() {
        scanner = new Scanner(System.in);
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
}
