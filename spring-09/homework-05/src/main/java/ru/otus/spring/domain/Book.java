package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Book {
    private final Long id;
    private final String name;
    private final String description;
    private final int yearOfRelease;
    private final Author author;
    private final Genre genre;

    public Book(String name, String description, int yearOfRelease, Author author, Genre genre) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.yearOfRelease = yearOfRelease;
        this.author = author;
        this.genre = genre;
    }
}
