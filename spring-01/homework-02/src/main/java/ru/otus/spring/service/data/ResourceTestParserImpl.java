package ru.otus.spring.service.data;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;
import ru.otus.spring.exception.FileParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class ResourceTestParserImpl implements ResourceTestParser {
    private static final String DELIMITER = ",";
    private static final String EMPTY_STR = "";

    @Override
    public StudentTest parseTest(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            int lineNumber = 0;
            String currentLine;
            String question = EMPTY_STR;
            List<String> availableAnswers = new ArrayList<>();
            List<StudentTestQuestion> testQuestions = new ArrayList<>();

            while ((currentLine = bufferedReader.readLine()) != null) {
                lineNumber++;
                List<String> tokens = parseCsvLine(currentLine);

                if (lineNumber == 1)  {
                    question = (tokens.isEmpty()) ? EMPTY_STR : tokens.get(0);
                } else if (lineNumber == 2) {
                    availableAnswers = tokens;
                } else {
                    lineNumber = 0;
                    if (!question.isEmpty() && !tokens.isEmpty()) {
                        testQuestions.add(
                                new StudentTestQuestion(
                                        question,
                                        availableAnswers,
                                        tokens));
                    }
                }
            }
            return new StudentTest(testQuestions);

        } catch (IOException e) {
            throw new FileParseException(e);
        }
    }

    private List<String> parseCsvLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken().trim();
            tokens.add(token);
        }
        return tokens;
    }
}
