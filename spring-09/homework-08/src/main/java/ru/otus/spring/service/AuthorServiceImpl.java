package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(
            AuthorRepository authorRepository,
            BookRepository bookRepository
    ) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Author add(String name) {
        return authorRepository.insert(new Author(name));
    }

    @Override
    public Author getOrAdd(String name) {
        Optional<Author> findAuthor = findByName(name);
        return findAuthor.orElseGet(() -> add(name));
    }

    @Override
    public Author save(Author author) {
        Author updatedAuthor = authorRepository.save(author);
        if (author.getId() != null) {
            List<Book> books = bookRepository.findByAuthor(author);
            books.forEach( book -> {
                book.setAuthor(updatedAuthor);
                bookRepository.save(book);
            });
        }
        return updatedAuthor;
    }

    @Override
    public void delete(Author author) {
        if (author.getId() != null) {
            if (bookRepository.existsByAuthor(author)) {
                throw new RuntimeException("Author can't be deleted due to the presence of books with the author = "
                        + author.getName());
            }
            authorRepository.delete(author);
        }
    }

    @Override
    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
