package com.blog.authentications.exceptions;

import com.blog.authentications.model.Level;
import com.blog.authentications.model.SystemError;
import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;


public class GeneralKnowException extends Exception {

    private SystemError systemError;
    public HttpStatusCode httpCode;
    private Logger logger;
    private Object data;
    private Level level;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public GeneralKnowException(String message) {
        super(message);
    }

    public GeneralKnowException(SystemError systemError, String message) {
        super(message);
        this.systemError = systemError;
    }

    public GeneralKnowException(SystemError systemError, String message, HttpStatusCode httpCode) {
        super(message);
        this.systemError = systemError;
        this.httpCode = httpCode;
    }

    public GeneralKnowException(SystemError systemError, String message, HttpStatusCode httpCode, Object data) {
        super(message);
        this.systemError = systemError;
        this.httpCode = httpCode;
        this.data = data;
    }

    public GeneralKnowException(SystemError systemError, HttpStatusCode httpCode, Object data) {
        this.systemError = systemError;
        this.httpCode = httpCode;
        this.data = data;
    }

    public GeneralKnowException(SystemError systemError, Logger logger, HttpStatusCode httpCode) {

        this.systemError = systemError;
        this.logger = logger;
        this.httpCode = httpCode;

    }

    public GeneralKnowException(SystemError systemError, Logger logger, Level error, String message,
            HttpStatusCode httpCode) {
        super(message);
        this.systemError = systemError;
        this.logger = logger;
        this.level = error;
        this.httpCode = httpCode;

    }

    public SystemError getSystemError() {
        return systemError;
    }

    public void setSystemError(SystemError systemError) {
        this.systemError = systemError;
    }

    public HttpStatusCode getHtppCode() {
        return httpCode;
    }

    public void setHtppCode(HttpStatusCode httpCode) {
        this.httpCode = httpCode;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
