package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookPageController.class)
public class BookPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        this.webTestClient = MockMvcWebTestClient
                .bindTo(mockMvc)
                .build();
    }

    @Test
    public void getBooksTest() throws Exception {
        EntityExchangeResult<byte[]> result = webTestClient
                .get()
                .uri("/")
                .exchange().expectStatus().isOk().expectBody().returnResult();
        MockMvcWebTestClient.resultActionsFor(result).andExpect(view().name("books"));
    }

    @Test
    public void addBookFormTest() throws Exception {
        EntityExchangeResult<byte[]> result = webTestClient
                .get()
                .uri("/add")
                .exchange().expectStatus().isOk().expectBody().returnResult();
        MockMvcWebTestClient.resultActionsFor(result).andExpect(view().name("add"));
    }

    @Test
    public void saveBookFormTest() throws Exception {
        EntityExchangeResult<byte[]> result = webTestClient
                .get()
                .uri("/edit?id=1L")
                .exchange().expectStatus().isOk().expectBody().returnResult();
        MockMvcWebTestClient.resultActionsFor(result)
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("book_id", "1L"));
    }
}
