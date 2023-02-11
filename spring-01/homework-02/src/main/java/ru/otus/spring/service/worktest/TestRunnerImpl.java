package ru.otus.spring.service.worktest;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;
import ru.otus.spring.service.data.IOContextWorker;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestRunnerImpl implements TestRunner {
    private static final String DELIMITER = ",";
    private static final String DOT = ") ";

    private final StudentTestDao studentTestDao;
    private final TestChecker testChecker;
    private final IOContextWorker ioContextWorker;
    private final TestPrinter testPrinter;

    public TestRunnerImpl(
            StudentTestDao studentTestDao,
            TestChecker testChecker,
            IOContextWorker ioContextWorker,
            TestPrinter testPrinter
    ) {
        this.studentTestDao = studentTestDao;
        this.testChecker = testChecker;
        this.ioContextWorker = ioContextWorker;
        this.testPrinter = testPrinter;
    }

    @Override
    public void runTest() {
        ioContextWorker.outputLine("Введите ваши данные");

        ioContextWorker.outputLine("Имя:");
        String firstName = ioContextWorker.getNextLine();

        ioContextWorker.outputLine("Фамилия:");
        String secondName = ioContextWorker.getNextLine();

        Student student = new Student(firstName, secondName);
        StudentTest studentTest = studentTestDao.getStudentTest();
        Map<String, List<String>> studentTestResults = new HashMap<>();

        ioContextWorker.outputLine("\nСтарт тестирования!");
        ioContextWorker.outputLine("Правильных ответов на каждый вопрос может быть один или несколько, " +
                "если ответов несколько то они должны быть разделены запятыми");

        int countQuestions = studentTest.getQuestions().size();
        int counterQuestion = 0;

        for (StudentTestQuestion testQuestion : studentTest.getQuestions()) {
            counterQuestion++;
            String question = testQuestion.getQuestion();
            ioContextWorker.outputLine("\nВопрос " + counterQuestion + "/"+ countQuestions + " : " + question);
            ioContextWorker.outputLine("Варианты ответов: ");
            int counterAvailableAnswer = 0;
            for (String availableAnswer : testQuestion.getAvailableAnswers()) {
                counterAvailableAnswer++;
                ioContextWorker.outputLine(counterAvailableAnswer + DOT + availableAnswer);
            }
            ioContextWorker.outputLine();
            ioContextWorker.outputLine("Ваш ответ:");
            String studentAnswers = ioContextWorker.getNextLine();
            String[] parsedAnswers = studentAnswers.split(DELIMITER);
            List<String> getAnswers;
            if (parsedAnswers.length != 0) {
                getAnswers = Arrays.stream(parsedAnswers)
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toList());
            } else {
                getAnswers = Collections.emptyList();
            }
            studentTestResults.put(question, getAnswers);
        }

        StudentTestResult studentTestResult = new StudentTestResult(studentTest, studentTestResults);
        boolean testIsPassed = testChecker.testIsPassed(studentTestResult);

        ioContextWorker.outputLine("\nСтудент " + student.getFullName() + ", результат: "
                + (testIsPassed? "тест успешно сдан!" : "неуспешная попытка :("));
        ioContextWorker.outputLine("\nПравильные варианты ответов теста:");
        testPrinter.printTest();
        ioContextWorker.outputLine("\nТестирование окончено!");
    }
}
