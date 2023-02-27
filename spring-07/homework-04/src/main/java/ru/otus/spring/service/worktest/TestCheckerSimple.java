package ru.otus.spring.service.worktest;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.domain.StudentTestResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestCheckerSimple implements TestChecker {
    @Override
    public boolean testIsPassed(StudentTestResult studentTestResult) {
        if (studentTestResult.getResults().isEmpty()) {
            return false;
        }

        StudentTest test = studentTestResult.getTest();
        Map<String, List<String>> studentResults = studentTestResult.getResults();

        for (StudentTestQuestion testQuestion : test.getQuestions()) {
            List<String> findResults = studentResults.getOrDefault(
                    testQuestion.getQuestion(),
                    Collections.emptyList());

            if (findResults.isEmpty()) {
                return false;
            }

            if (!questionIsPassed(findResults, testQuestion.getRightAnswers())) {
                return false;
            }
        }

        return true;
    }

    private static boolean questionIsPassed(
            List<String> studentAnswers,
            List<String> testRightAnswers
    ) {
        if (studentAnswers.size() == testRightAnswers.size()) {
            return getLowerCaseSet(testRightAnswers).equals(getLowerCaseSet(studentAnswers));
        }
        return false;
    }

    private static Set<String> getLowerCaseSet(List<String> src) {
        return src.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
