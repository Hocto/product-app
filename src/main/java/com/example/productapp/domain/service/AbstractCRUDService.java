package com.example.productapp.domain.service;

public interface AbstractCRUDService<T, R> {

    T retrieve(R uniqueField);

    T add(T t);

    T modify(T t);

    void remove(R r);
}
