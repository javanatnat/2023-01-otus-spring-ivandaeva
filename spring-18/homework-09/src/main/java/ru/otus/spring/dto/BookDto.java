package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookDto {
    private Long id;
    private String name;
    private String description;
    private int year;
    private String authorName;
    private String genreName;

    public static Book toDomainObject(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .name(bookDto.getName())
                .description(bookDto.getDescription())
                .yearOfRelease(bookDto.getYear())
                .author(new Author(bookDto.getAuthorName()))
                .genre(new Genre(bookDto.getGenreName()))
                .build();
    }

    public static BookDto toDto(Book book) {
        return new BookDtoBuilder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .year(book.getYearOfRelease())
                .authorName(book.getAuthor().getName())
                .genreName(book.getGenre().getName())
                .build();
    }
}
