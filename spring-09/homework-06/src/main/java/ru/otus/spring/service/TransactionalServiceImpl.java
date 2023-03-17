package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionalServiceImpl implements TransactionalService {
    @Override
    @Transactional
    public <T> T doInTransaction(Supplier<T> action) {
        return action.get();
    }
}
