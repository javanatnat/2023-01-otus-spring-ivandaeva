package ru.otus.spring.service.worktest;

import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;
import ru.otus.spring.exception.TestExecutionException;
import ru.otus.spring.service.data.DataReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TestRunnerImpl implements TestRunner {
    private static final String DELIMITER = ",";
    private static final String DOT = ") ";

    private final StudentTestDao studentTestDao;
    private final TestChecker testChecker;
    private final DataReader dataReader;

    public TestRunnerImpl(
            StudentTestDao studentTestDao,
            TestChecker testChecker,
            DataReader dataReader
    ) {
        this.studentTestDao = studentTestDao;
        this.testChecker = testChecker;
        this.dataReader = dataReader;
    }

    @Override
    public void runTest() {
        try {
            run();
        } catch (IOException e) {
            throw new TestExecutionException(e);
        }
    }

    public void run() throws IOException {

        System.out.println("Введите ваши данные");

        System.out.println("Имя:");
        String firstName = dataReader.getNextLine();

        System.out.println("Фамилия:");
        String secondName = dataReader.getNextLine();

        Student student = new Student(firstName, secondName);
        StudentTest studentTest = studentTestDao.getStudentTest();
        Map<String, List<String>> studentTestResults = new HashMap<>();

        System.out.println("\nСтарт тестирования!");
        System.out.println("Правильных ответов на каждый вопрос может быть один или несколько, " +
                "если ответов несколько то они должны быть разделены запятыми");

        int countQuestions = studentTest.getQuestions().size();
        int counterQuestion = 0;

        for (StudentTestQuestion testQuestion : studentTest.getQuestions()) {
            counterQuestion++;
            String question = testQuestion.getQuestion();
            System.out.println("\nВопрос " + counterQuestion + "/"+ countQuestions + " : " + question);
            System.out.println("Варианты ответов: ");
            int counterAvailableAnswer = 0;
            for (String availableAnswer : testQuestion.getAvailableAnswers()) {
                counterAvailableAnswer++;
                System.out.println(counterAvailableAnswer + DOT + availableAnswer);
            }
            System.out.println();
            System.out.println("Ваш ответ:");
            String studentAnswers = dataReader.getNextLine();
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

        System.out.println("\nСтудент " + student.getFullName() + ", результат: "
                + (testIsPassed? "тест успешно сдан!" : "неуспешная попытка :("));
        System.out.println("\nТестирование окончено!");
    }
}
