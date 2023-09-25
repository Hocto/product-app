package com.example.productapp.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends StockException {
    private final String entity;

    public EntityNotFoundException(String message, String entity) {
        super(message);
        this.entity = entity;
    }
}
