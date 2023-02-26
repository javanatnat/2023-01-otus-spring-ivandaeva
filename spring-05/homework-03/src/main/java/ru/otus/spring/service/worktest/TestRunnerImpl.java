package ru.otus.spring.service.worktest;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;
import ru.otus.spring.service.data.IOContextWorker;
import ru.otus.spring.service.data.TestMessageKey;
import ru.otus.spring.service.data.TestMessages;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestRunnerImpl implements TestRunner {
    private static final String SPACE = " ";
    private static final String COLON = ":";
    private static final String COMMA = ",";
    private static final String DOT = ") ";
    private static final String END_LINE = "\n";
    private final StudentTestDao studentTestDao;
    private final TestChecker testChecker;
    private final IOContextWorker ioContextWorker;
    private final TestPrinter testPrinter;
    private final TestMessages testMessages;

    public TestRunnerImpl(
            StudentTestDao studentTestDao,
            TestChecker testChecker,
            IOContextWorker ioContextWorker,
            TestPrinter testPrinter,
            TestMessages testMessages
    ) {
        this.studentTestDao = studentTestDao;
        this.testChecker = testChecker;
        this.ioContextWorker = ioContextWorker;
        this.testPrinter = testPrinter;
        this.testMessages = testMessages;
    }

    @Override
    public void runTest() {
        ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_ENTER_DATA));

        ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_FIRSTNAME) + COLON);
        String firstName = ioContextWorker.getNextLine();

        ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_SECONDNAME) + COLON);
        String secondName = ioContextWorker.getNextLine();

        Student student = new Student(firstName, secondName);
        StudentTest studentTest = studentTestDao.getStudentTest();
        Map<String, List<String>> studentTestResults = new HashMap<>();

        ioContextWorker.outputLine(END_LINE + getMessage(TestMessageKey.TEST_RUN_START));
        ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_DESCRIPTION));

        int countQuestions = studentTest.getQuestions().size();
        int counterQuestion = 0;

        for (StudentTestQuestion testQuestion : studentTest.getQuestions()) {
            counterQuestion++;
            String question = testQuestion.getQuestion();
            ioContextWorker.outputLine(END_LINE + getMessage(TestMessageKey.TEST_RUN_QUESTION)
                    + " " + counterQuestion + "/"+ countQuestions
                    + " " + COLON + " " + question);
            ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_AVAILABLE_ANSWERS) + COLON + SPACE);
            int counterAvailableAnswer = 0;
            for (String availableAnswer : testQuestion.getAvailableAnswers()) {
                counterAvailableAnswer++;
                ioContextWorker.outputLine(counterAvailableAnswer + DOT + availableAnswer);
            }
            ioContextWorker.outputLine();
            ioContextWorker.outputLine(getMessage(TestMessageKey.TEST_RUN_STUDENT_ANSWER) + COLON);
            String studentAnswers = ioContextWorker.getNextLine();
            String[] parsedAnswers = studentAnswers.split(COMMA);
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

        ioContextWorker.outputLine(END_LINE + getMessage(TestMessageKey.TEST_RUN_STUDENT)
                + SPACE + student.getFullName()
                + COMMA + SPACE + getMessage(TestMessageKey.TEST_RUN_RESULT) + COLON + SPACE
                + (testIsPassed? getMessage(TestMessageKey.TEST_RUN_SUCCESS_RESULT) : getMessage(TestMessageKey.TEST_RUN_UNSUCCESS_RESULT)));
        ioContextWorker.outputLine(END_LINE + getMessage(TestMessageKey.TEST_RUN_CORRECT_ANSWERS) + COLON);
        testPrinter.printTest();
        ioContextWorker.outputLine(END_LINE + getMessage(TestMessageKey.TEST_RUN_END));
    }

    private String getMessage(TestMessageKey key) {
        return testMessages.getMessage(key);
    }
}
