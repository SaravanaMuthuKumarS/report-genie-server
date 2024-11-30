package com.i2i.rgs.helper;


import java.util.NoSuchElementException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 *   Global exception handler class that handles exceptions thrown by the application.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgument(Exception e) {
        return ErrorResponse.setErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        return ErrorResponse.setErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        return ErrorResponse.setErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.setErrorResponse(e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserServiceException(Exception e) {
        return ErrorResponse.setErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception e) {
        return ErrorResponse.setErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {RGSException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleException(RGSException e) {
        String message = "Error occurred with the server. " + e.getMessage();
        return ErrorResponse.setErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}