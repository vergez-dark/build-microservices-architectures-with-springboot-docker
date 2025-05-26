package com.jeffrey.POST_SERVICE.exceptions;

import org.springframework.http.HttpStatus;

public class PostServiceExeption  extends RuntimeException {
    private HttpStatus status;
    private String message;

    public PostServiceExeption(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public PostServiceExeption(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
}
