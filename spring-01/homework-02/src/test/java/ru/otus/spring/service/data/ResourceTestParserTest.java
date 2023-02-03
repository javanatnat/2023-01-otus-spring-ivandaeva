package ru.otus.spring.service.data;

import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.StudentTest;
import ru.otus.spring.domain.StudentTestQuestion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceTestParserTest {
    private final ResourceTestParser parser = new ResourceTestParserImpl();

    @Test
    void testParseEmptyTest() {
        String testStr = "test";
        InputStream testStream = new ByteArrayInputStream(testStr.getBytes());
        assertThat(parser.parseTest(testStream)).isEqualTo(StudentTest.emptyTest());
    }

    @Test
    void testParseTest() {
        String testStr = "test\nq,e,t,u\nu\n";
        InputStream testStream = new ByteArrayInputStream(testStr.getBytes());
        assertThat(parser.parseTest(testStream)).isEqualTo(
                new StudentTest(List.of(
                        new StudentTestQuestion("test", List.of("q", "e", "t", "u"), List.of("u")))));
    }

    @Test
    void testParseTestManyRows() {
        String testStr = "test\nq,e,t,u\nu\ntest2\nq,e,t,u\nu\ntest3\nq,e,t,u\nu\n";
        InputStream testStream = new ByteArrayInputStream(testStr.getBytes());
        assertThat(parser.parseTest(testStream)).isEqualTo(
                new StudentTest(List.of(
                        new StudentTestQuestion("test", List.of("q", "e", "t", "u"), List.of("u")),
                        new StudentTestQuestion("test2", List.of("q", "e", "t", "u"), List.of("u")),
                        new StudentTestQuestion("test3", List.of("q", "e", "t", "u"), List.of("u"))
                        )));
    }

    @Test
    void testParseTestManyRowsWithError() {
        String testStr = "test\nq,e,t,u\nu\n\nq,e,t,u\nu\ntest3\nq,e,t,u\nu\n\n\n";
        InputStream testStream = new ByteArrayInputStream(testStr.getBytes());
        assertThat(parser.parseTest(testStream)).isEqualTo(
                new StudentTest(List.of(
                        new StudentTestQuestion("test", List.of("q", "e", "t", "u"), List.of("u")),
                        new StudentTestQuestion("test3", List.of("q", "e", "t", "u"), List.of("u"))
                )));
    }
}
