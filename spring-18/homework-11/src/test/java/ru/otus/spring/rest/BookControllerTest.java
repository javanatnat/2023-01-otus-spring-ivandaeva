package ru.otus.spring.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.controller.NotFoundException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.BookDtoConverter;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @Autowired
    private BookDtoConverter converter;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getBooksTest() {
        Book book = Book.builder()
                .id("1")
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author("1", "author"))
                .genre(new Genre("1", "genre"))
                .build();

        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        webTestClient.get()
                .uri("/api/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class).contains(converter.toDto(book));
    }

    @Test
    public void getBookTest() {
        Book book = Book.builder()
                .id("1")
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author("1L", "author"))
                .genre(new Genre("1L", "genre"))
                .build();

        when(bookRepository.findById("1")).thenReturn(Mono.just(book));

        webTestClient.get()
                .uri("/api/book/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class).isEqualTo(converter.toDto(book));
    }

    @Test
    public void addBookTest() {
        Book book = Book.builder()
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author("1L", "author"))
                .genre(new Genre("1L", "genre"))
                .build();

        Book added = Book.builder()
                .id("1L")
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author("1L", "author"))
                .genre(new Genre("1L", "genre"))
                .build();

        when(authorRepository.findByName(anyString())).thenReturn(Mono.empty());
        when(genreRepository.findByName(anyString())).thenReturn(Mono.empty());

        when(authorRepository.insert(any(Author.class))).thenReturn(Mono.just(new Author("1L", "author")));
        when(genreRepository.insert(any(Genre.class))).thenReturn(Mono.just(new Genre("1L", "genre")));

        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(added));

        Flux<BookDto> result = webTestClient.post()
                .uri("/api/book")
                .bodyValue(converter.toDto(book))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        BookDto bookDtoResult = result.blockLast();
        assertThat(bookDtoResult).isEqualTo(converter.toDto(added));
    }

    @Test
    public void saveBookTest() {
        Book oldBook = Book.builder()
                .id("1L")
                .name("book")
                .description("description old")
                .yearOfRelease(3032)
                .author(new Author("1L", "author"))
                .genre(new Genre("1L", "genre"))
                .build();

        Book book = Book.builder()
                .id("1L")
                .name("book")
                .description("description")
                .yearOfRelease(3030)
                .author(new Author("1L", "author"))
                .genre(new Genre("1L", "genre"))
                .build();

        when(authorRepository.findByName(anyString())).thenReturn(Mono.just(new Author("1L", "author")));
        when(genreRepository.findByName(anyString())).thenReturn(Mono.just(new Genre("1L", "genre")));

        when(authorRepository.insert(any(Author.class))).thenReturn(Mono.empty());
        when(genreRepository.insert(any(Genre.class))).thenReturn(Mono.empty());

        when(bookRepository.findById(anyString())).thenReturn(Mono.just(oldBook));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        Flux<BookDto> result = webTestClient.put()
                .uri("/api/book/1L")
                .bodyValue(converter.toDto(book))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        BookDto bookDtoResult = result.blockLast();
        assertThat(bookDtoResult).isEqualTo(converter.toDto(book));
    }

    @Test
    public void deleteBookTest() {
        when(bookRepository.deleteById(anyString())).then(v -> null);

        webTestClient.delete()
                .uri("/api/book/1L")
                .exchange()
                .expectStatus().isOk().expectBody().isEmpty();
    }

    @Test
    public void internalServerErrorTest() {
        when(bookRepository.deleteById(anyString())).thenThrow(NotFoundException.class);
        webTestClient.delete()
                .uri("/api/book/1L")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void notFoundErrorTest() {
        when(bookRepository.deleteById(anyString())).thenThrow(IllegalArgumentException.class);
        webTestClient.delete()
                .uri("/api/book/1L")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
