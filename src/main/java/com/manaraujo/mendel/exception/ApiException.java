package com.manaraujo.mendel.exception;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private Integer statusCode;

    public ApiException(String code, String description, Integer statusCode) {
        super(description);
        this.code = code;
        this.description = description;
        this.statusCode = statusCode;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

}
