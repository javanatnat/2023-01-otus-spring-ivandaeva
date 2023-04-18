package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService bookService;

    @Test
    public void getBooksTest() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();
        when(bookService.findAll()).thenReturn(List.of(book));
        List<BookDto> expected = List.of(BookDto.toDto(book));

        MvcResult mvcResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expected))
                .andReturn();
    }

    @Test
    public void addBookFormTest() throws Exception {
        mvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }

    @Test
    public void addBookTest() throws Exception {
        Book book = Book.builder()
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();

        when(bookService.add(any(Book.class))).thenReturn(book);
        mvc.perform(post("/add")
                        .flashAttr("book", BookDto.toDto(book)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService, times(1)).add(any(Book.class));
    }

    @Test
    public void saveBookFormTest() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mvc.perform(get("/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("book", BookDto.toDto(book)));
    }

    @Test
    public void saveBookFormErrorTest() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/edit")
                        .param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveBookTest() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();
        when(bookService.update(any(Book.class))).thenReturn(book);
        mvc.perform(post("/edit")
                        .flashAttr("book", BookDto.toDto(book)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    public void saveBookErrorTest() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();
        when(bookService.update(any(Book.class))).thenThrow(IllegalArgumentException.class);
        mvc.perform(post("/edit")
                        .flashAttr("book", BookDto.toDto(book)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteBookTest() throws Exception {
        doNothing().when(bookService).delete(any(Book.class));
        mvc.perform(get("/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
