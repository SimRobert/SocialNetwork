package com.example.lab8_9_map.validators;
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}