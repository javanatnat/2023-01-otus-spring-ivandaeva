package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.service.data.IOContextWorker;
import ru.otus.spring.service.worktest.TestPrinter;
import ru.otus.spring.service.worktest.TestRunner;

@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);

        TestPrinter testPrinter = context.getBean(TestPrinter.class);
        testPrinter.printTest();

        IOContextWorker ioContextWorker = context.getBean(IOContextWorker.class);
        ioContextWorker.outputLine();

        TestRunner testRunner = context.getBean(TestRunner.class);
        testRunner.runTest();
    }
}
