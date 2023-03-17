package ru.otus.spring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("genre"),
                @NamedAttributeNode("comments")
        })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "release_year")
    private int yearOfRelease;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Author author;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Genre genre;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookComment> comments;

    public Book() {
        comments = new ArrayList<>();
    }

    public Book(
            String name,
            String description,
            int yearOfRelease,
            Author author,
            Genre genre
    ) {
        this(name, description, yearOfRelease, author, genre, new ArrayList<>());
    }

    public Book(
            String name,
            String description,
            int yearOfRelease,
            Author author,
            Genre genre,
            List<BookComment> comments
    ) {
        this(null, name, description, yearOfRelease, author, genre, comments);
    }

    public Book(
            Long id,
            String name,
            String description,
            int yearOfRelease,
            Author author,
            Genre genre,
            List<BookComment> comments
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearOfRelease = yearOfRelease;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", author=" + author +
                ", genre=" + genre +
                ", comments="
                + comments.stream()
                .map(c -> "id=" + c.getId() + ", text=" + c.getText())
                .collect(Collectors.joining(";","{","}")) +
                '}';
    }
}
