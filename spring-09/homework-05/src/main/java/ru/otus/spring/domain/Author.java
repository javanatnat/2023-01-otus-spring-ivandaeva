package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Author {
    private final Long id;
    private final String name;

    public Author(String name) {
        this.id = null;
        this.name = name;
    }
}
