package ru.otus.spring.dto;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

@Component
public class BookDtoConverter {
    public Book toDomainObject(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .name(bookDto.getName())
                .description(bookDto.getDescription())
                .yearOfRelease(bookDto.getYear())
                .author(new Author(bookDto.getAuthorName()))
                .genre(new Genre(bookDto.getGenreName()))
                .build();
    }

    public BookDto toDto(Book book) {
        return new BookDto.BookDtoBuilder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .year(book.getYearOfRelease())
                .authorName(book.getAuthor().getName())
                .genreName(book.getGenre().getName())
                .build();
    }

    public BookDto toDtoFromOptional(Optional<Book> book) {
        if (book.isEmpty()) {
            return new BookDto();
        }
        return toDto(book.get());
    }
}
