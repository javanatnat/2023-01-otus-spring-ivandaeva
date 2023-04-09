package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String name;
    private String description;
    private int yearOfRelease;
    private String authorName;
    private String genreName;
}
