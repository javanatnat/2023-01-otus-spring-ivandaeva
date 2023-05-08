package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String name;
    private String description;
    private int yearOfRelease;
    private Author author;
    private Genre genre;

    public Book(
            String id,
            String name,
            String description,
            int yearOfRelease,
            Author author,
            Genre genre
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearOfRelease = yearOfRelease;
        this.author = author;
        this.genre = genre;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Book(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.yearOfRelease = builder.yearOfRelease;
        this.author = builder.author;
        this.genre = builder.genre;
    }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private int yearOfRelease;
        private Author author;
        private Genre genre;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public  Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder yearOfRelease(int yearOfRelease) {
            this.yearOfRelease = yearOfRelease;
            return this;
        }

        public Builder author(Author author) {
            this.author = author;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
