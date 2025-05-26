package com.blog.notifications.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class GeneralException extends Exception {
    private String code;
    private String message;
    private String status;

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(String code, String message, String status) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public GeneralException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
