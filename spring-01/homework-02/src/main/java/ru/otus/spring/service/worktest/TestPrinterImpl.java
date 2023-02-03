package ru.otus.spring.service.worktest;

import ru.otus.spring.dao.StudentTestDao;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;

public class TestPrinterImpl implements TestPrinter {
    private static final char START_LETTER = 'A';
    private static final String DOT = ". ";
    private static final String ANSWERS_START_TAB = "    ";
    private static final String ANSWERS_TAB = "   ";


    private final StudentTestDao studentTestDao;

    public TestPrinterImpl(StudentTestDao studentTestDao) {
        this.studentTestDao = studentTestDao;
    }

    @Override
    public void printTest() {
        StudentTest studentTest = studentTestDao.getStudentTest();
        int questionNum = 0;
        for(StudentTestQuestion studentTestQuestion : studentTest.getQuestions()) {
            questionNum++;
            System.out.println(questionNum + DOT + studentTestQuestion.getQuestion());
            System.out.print(ANSWERS_START_TAB);
            char answerLetter = START_LETTER;
            for(String availableAnswer : studentTestQuestion.getAvailableAnswers()) {
                System.out.print(answerLetter + DOT + availableAnswer + ANSWERS_TAB);
                answerLetter++;
            }
            System.out.println();
        }
    }
}
