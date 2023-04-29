package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.controller.NotFoundException;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.BookDtoConverter;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookDtoConverter converter;
    private final BookService bookService;

    @GetMapping("/api/book")
    public List<BookDto> getBooks() {
        return bookService.findAll().stream().map(converter::toDto).toList();
    }

    @GetMapping("/api/book/{id}")
    public BookDto getBook(@PathVariable("id") Long id) {
        Optional<Book> findBook = bookService.findById(id);
        return converter.toDtoFromOptional(findBook);
    }

    @PostMapping("/api/book")
    public BookDto addBook(@RequestBody BookDto bookDto) {
        Book book = converter.toDomainObject(bookDto);
        Book added = bookService.add(book);
        return converter.toDto(added);
    }

    @PutMapping("/api/book/{id}")
    public BookDto saveBook(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        Book book = converter.toDomainObject(bookDto);
        Book updated = bookService.update(book);
        return converter.toDto(updated);
    }

    @DeleteMapping("/api/book/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.delete(Book.builder().id(id).build());
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
