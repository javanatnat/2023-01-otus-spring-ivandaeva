package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.controller.NotFoundException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.BookDtoConverter;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(BookDtoConverter.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService bookService;
    @Autowired
    private BookDtoConverter converter;
    @Autowired
    private ObjectMapper mapper;

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
        List<BookDto> expected = List.of(converter.toDto(book));

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)))
                .andReturn();
    }

    @Test
    public void getBookTest() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        BookDto expected = converter.toDto(book);

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)))
                .andReturn();
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

        Book added = Book.builder()
                .id(1L)
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author(1L, "author"))
                .genre(new Genre(1L, "genre"))
                .build();

        when(bookService.add(any(Book.class))).thenReturn(added);
        mvc.perform(post("/api/book")
                        .content(mapper.writeValueAsString(converter.toDto(book)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(converter.toDto(added))));
        verify(bookService, times(1)).add(any(Book.class));
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
        mvc.perform(put("/api/book/1")
                        .content(mapper.writeValueAsString(converter.toDto(book)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(converter.toDto(book))));
        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    public void deleteBookTest() throws Exception {
        doNothing().when(bookService).delete(any(Book.class));
        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).delete(any(Book.class));
    }

    @Test
    public void internalServerErrorTest() throws Exception {
        doThrow(IllegalArgumentException.class).when(bookService).delete(any(Book.class));
        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isInternalServerError());
        verify(bookService, times(1)).delete(any(Book.class));
    }

    @Test
    public void notFoundErrorTest() throws Exception {
        doThrow(NotFoundException.class).when(bookService).delete(any(Book.class));
        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isNotFound());
        verify(bookService, times(1)).delete(any(Book.class));
    }
}
