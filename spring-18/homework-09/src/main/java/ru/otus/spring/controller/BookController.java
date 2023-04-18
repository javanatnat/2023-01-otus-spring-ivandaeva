package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String getBooks(Model model) {
        List<Book> books = bookService.findAll();
        List<BookDto> bookDtos = books.stream().map(BookDto::toDto).toList();
        model.addAttribute("books", bookDtos);
        return "books";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        BookDto bookDto = new BookDto();
        model.addAttribute("book", bookDto);
        return "add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") BookDto bookDto) {
        System.out.println("bookDto=" + bookDto);
        Book book = BookDto.toDomainObject(bookDto);
        System.out.println("book=" + book);
        bookService.add(book);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String saveBookForm(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new NotFoundException(
                "Book with id = " + id + " has not found in library"));
        model.addAttribute("book", BookDto.toDto(book));
        return "edit";
    }

    @PostMapping("/edit")
    public String saveBook(@ModelAttribute("book") BookDto bookDto) {
        Book book = BookDto.toDomainObject(bookDto);
        bookService.update(book);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.delete(Book.builder().id(id).build());
        return "redirect:/";
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
