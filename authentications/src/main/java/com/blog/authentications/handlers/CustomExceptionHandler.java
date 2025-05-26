package com.blog.authentications.handlers;

import com.blog.authentications.exceptions.GeneralException;
import com.blog.authentications.exceptions.NoActiveUserException;
import com.blog.authentications.exceptions.RessourceNotFoundException;
import com.blog.authentications.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoActiveUserException.class)
    public ResponseEntity<ApiError> handleNoActiveUserException(NoActiveUserException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setCode(HttpStatus.FORBIDDEN.value());
        System.out.println(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ApiError> handlerGeneralException(RessourceNotFoundException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setCode(HttpStatus.NOT_FOUND.value());
        System.out.println(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
