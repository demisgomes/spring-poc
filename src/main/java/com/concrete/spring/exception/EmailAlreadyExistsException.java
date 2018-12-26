package com.concrete.spring.exception;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(){
        super("This email already exists");
        super.printStackTrace();
    }
}
