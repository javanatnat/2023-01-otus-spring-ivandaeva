package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.controller.NotFoundException;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.BookDtoConverter;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookDtoConverter converter;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/api/book")
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(converter::toDto);
    }

    @GetMapping("/api/book/{id}")
    public Mono<BookDto> getBook(@PathVariable("id") String id) {
        return bookRepository.findById(id).map(converter::toDto);
    }

    @PostMapping("/api/book")
    public Mono<BookDto> addBook(@RequestBody BookDto bookDto) {
        Book book = converter.toDomainObject(bookDto);
        return authorRepository
                .findByName(book.getAuthor().getName())
                .switchIfEmpty(authorRepository.insert(book.getAuthor()))
                .doOnNext(book::setAuthor)
                .flatMap(a -> genreRepository.findByName(book.getGenre().getName()))
                .switchIfEmpty(genreRepository.insert(book.getGenre()))
                .doOnNext(book::setGenre)
                .flatMap(g -> bookRepository.save(book))
                .map(converter::toDto)
                .onErrorReturn(new BookDto());
    }

    @PutMapping("/api/book/{id}")
    public Mono<BookDto> saveBook(@PathVariable("id") String id, @RequestBody BookDto bookDto) {
        Book book = converter.toDomainObject(bookDto);
        return bookRepository.findById(id)
                .flatMap(b -> authorRepository.findByName(book.getAuthor().getName()))
                .switchIfEmpty(authorRepository.insert(book.getAuthor()))
                .doOnNext(book::setAuthor)
                .flatMap(a -> genreRepository.findByName(book.getGenre().getName()))
                .switchIfEmpty(genreRepository.insert(book.getGenre()))
                .doOnNext(book::setGenre)
                .flatMap(g -> bookRepository.save(book))
                .map(converter::toDto)
                .defaultIfEmpty(new BookDto())
                .onErrorReturn(new BookDto());
    }

    @DeleteMapping("/api/book/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND,
            reason="Book has not found in library")
    @ExceptionHandler(NotFoundException.class)
    public void notFound(Exception ex) {
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR,
            reason="Undefined server error")
    @ExceptionHandler(IllegalArgumentException.class)
    public void serverError() {
    }
}
