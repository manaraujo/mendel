package com.manaraujo.mendel.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        ApiError apiError = ApiError.builder()
                .error("missing_parameter")
                .message(message)
                .status(BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handlerHttpRequestMethodNotSupportedException(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
        ApiError apiError = ApiError.builder()
                .error("method_not_allowed")
                .message(String.format("%s. Route %s", ex.getMessage(), req.getRequestURI()))
                .status(METHOD_NOT_ALLOWED.value())
                .build();

        return ResponseEntity.status(METHOD_NOT_ALLOWED.value()).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handlerNotFoundException(NotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .error("not_found")
                .message(ex.getMessage())
                .status(NOT_FOUND.value())
                .build();

        return ResponseEntity.status(NOT_FOUND.value()).body(apiError);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiError> handleApiException(ApiException apiException) {
        Integer statusCode = apiException.getStatusCode();

        ApiError apiError = ApiError.builder()
                .error(apiException.getCode())
                .message(apiException.getDescription())
                .status(statusCode)
                .build();

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
        ApiError apiError = ApiError.builder()
                .error("internal_error")
                .message(e.getMessage())
                .status(INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}
