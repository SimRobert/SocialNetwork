package com.example.lab8_9_map.validators;

public class ValidationException extends RuntimeException {
    String message;
    public ValidationException(String message) {
        super(message);
        this.message = message;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ValidationException(Throwable cause) {
        super(cause);
        this.message = message;
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }

    @Override
    public String toString() {
        return message + '\n';
    }
}
