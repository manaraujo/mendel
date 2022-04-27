package com.manaraujo.mendel.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    private static final String ERROR_CODE = "not_found";
    private static final String DEFAULT_MESSAGE = "Resource not found";

    public NotFoundException() {
        super(ERROR_CODE, DEFAULT_MESSAGE, HttpStatus.NOT_FOUND.value());
    }

    public NotFoundException(String description) {
        super(ERROR_CODE, description, HttpStatus.NOT_FOUND.value());
    }

}
