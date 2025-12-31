package com.spring.otmanagement.exception;

public class AppException extends RuntimeException{
    private int status;
    private String error;

    public AppException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
