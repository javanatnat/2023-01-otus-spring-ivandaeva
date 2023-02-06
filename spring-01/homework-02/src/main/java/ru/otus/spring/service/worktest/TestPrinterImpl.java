package ru.otus.spring.service.worktest;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.service.data.IOContextWorker;

@Service
public class TestPrinterImpl implements TestPrinter {
    private static final char START_LETTER = 'A';
    private static final String DOT = ". ";
    private static final String ANSWERS_START_TAB = "    ";
    private static final String ANSWERS_TAB = "   ";


    private final StudentTestDao studentTestDao;
    private final IOContextWorker ioContextWorker;

    public TestPrinterImpl(
            StudentTestDao studentTestDao,
            IOContextWorker ioContextWorker
    ) {
        this.studentTestDao = studentTestDao;
        this.ioContextWorker = ioContextWorker;
    }

    @Override
    public void printTest() {
        ioContextWorker.outputLine("Test:");

        StudentTest studentTest = studentTestDao.getStudentTest();
        int questionNum = 0;

        for(StudentTestQuestion studentTestQuestion : studentTest.getQuestions()) {
            questionNum++;
            ioContextWorker.outputLine(questionNum + DOT + studentTestQuestion.getQuestion());
            StringBuilder buffer = new StringBuilder(ANSWERS_START_TAB);
            char answerLetter = START_LETTER;

            for(String availableAnswer : studentTestQuestion.getAvailableAnswers()) {
                buffer.append(answerLetter);
                buffer.append(DOT);
                buffer.append(availableAnswer);
                buffer.append(ANSWERS_TAB);
                answerLetter++;
            }

            ioContextWorker.outputLine(buffer.toString());
            ioContextWorker.outputLine();
        }
    }
}
