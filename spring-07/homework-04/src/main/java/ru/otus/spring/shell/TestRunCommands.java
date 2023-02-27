package ru.otus.spring.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.service.worktest.TestRunner;

@ShellComponent
public class TestRunCommands {
    private final TestRunner testRunner;

    @Autowired
    public TestRunCommands(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    @ShellMethod(value = "Run test!", key = {"r"})
    public void run() {
        testRunner.runTest();
    }

    @ShellMethod(value = "Smoke test", key = {"t", "s", "smoke"})
    public String test(@ShellOption(defaultValue = "no message :(") String test) {
        return "Test message from you: " + test;
    }
}
