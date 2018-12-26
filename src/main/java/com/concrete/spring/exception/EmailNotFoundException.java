package com.concrete.spring.exception;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException(){
        super("Invalid user and/or password");
        super.printStackTrace();
    }
}
