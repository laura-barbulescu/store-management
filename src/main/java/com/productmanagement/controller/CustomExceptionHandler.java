package com.productmanagement.controller;

import com.productmanagement.model.error.ErrorResponseBody;
import com.productmanagement.model.error.Errors;
import com.productmanagement.model.error.Error;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

/**
 * Exception handler for REST requests
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseBody> handleExceptions(final Exception exception) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception);

        return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponseBody> handleExceptions(final Exception exception) {
//        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception);
//
//        return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            NoSuchElementException.class
    })
    public ResponseEntity<ErrorResponseBody> handleExceptions(final RuntimeException exception) {

        return handleException(exception, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponseBody> missingMandatoryParameters(final RuntimeException servletException) {
        return handleException(servletException, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<ErrorResponseBody> methodNotAllowed(final ServletException servletException) {
        return handleException(servletException, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private ResponseEntity<ErrorResponseBody> handleException(final Exception exception, final HttpStatus status) {

        log.error("Something went wrong",exception);

        final Errors errors = new Errors();

        errors.addErrorItem(createError(status.getReasonPhrase(), exception.getLocalizedMessage()));
        return new ResponseEntity<>(new ErrorResponseBody(errors), status);
    }

    private Error createError(String reasonCode, final String errorDetails) {
        final Error error = new Error();

        error.setDescription(errorDetails);
        error.setReasonCode(reasonCode);

        return error;
    }





}
