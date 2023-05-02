package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookPageController.class)
public class BookPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getBooksTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andReturn();
    }

    @Test
    public void addBookFormTest() throws Exception {
        mvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }

    @Test
    public void saveBookFormTest() throws Exception {
        mvc.perform(get("/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("book_id", 1L));
    }
}
