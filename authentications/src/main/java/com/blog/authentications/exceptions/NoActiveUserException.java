package com.blog.authentications.exceptions;

public class NoActiveUserException extends RuntimeException {
    public NoActiveUserException(String message) {
        super(message);
    }
}
