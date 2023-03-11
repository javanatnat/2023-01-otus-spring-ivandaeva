package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Genre {
    private final Long id;
    private final String name;

    public Genre(String name) {
        this.id = null;
        this.name = name;
    }
}
