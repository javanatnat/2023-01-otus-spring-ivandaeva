package ru.otus.spring.service.data;

public enum TestMessageKey {
    TEST_PRINT_NAME("test.print.name"),
    TEST_PRINT_ANSWERS("test.print.answers"),
    TEST_RUN_ENTER_DATA("test.run.enter_data"),
    TEST_RUN_FIRSTNAME("test.run.firstname"),
    TEST_RUN_SECONDNAME("test.run.secondname"),
    TEST_RUN_START("test.run.start"),
    TEST_RUN_DESCRIPTION("test.run.description"),
    TEST_RUN_QUESTION("test.run.question"),
    TEST_RUN_AVAILABLE_ANSWERS("test.run.available_answers"),
    TEST_RUN_STUDENT_ANSWER("test.run.student_answer"),
    TEST_RUN_STUDENT("test.run.student"),
    TEST_RUN_RESULT("test.run.result"),
    TEST_RUN_SUCCESS_RESULT("test.run.success_result"),
    TEST_RUN_UNSUCCESS_RESULT("test.run.unsuccess_result"),
    TEST_RUN_CORRECT_ANSWERS("test.run.correct_answers"),
    TEST_RUN_END("test.run.end");
    private final String keyName;

    TestMessageKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
