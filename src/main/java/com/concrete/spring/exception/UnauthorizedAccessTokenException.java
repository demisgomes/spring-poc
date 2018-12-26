package com.concrete.spring.exception;

public class UnauthorizedAccessTokenException extends Exception {
    public UnauthorizedAccessTokenException(){
        super("Unauthorized access");
        super.printStackTrace();
    }
}
