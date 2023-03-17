package ru.otus.spring.service;

import java.util.function.Supplier;

public interface TransactionalService {
    <T> T doInTransaction(Supplier<T> action);
}
