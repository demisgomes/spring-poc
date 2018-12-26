package com.concrete.spring.exception;

public class InvalidUserPasswordException extends Exception {
    public InvalidUserPasswordException(){
        super("Invalid user and/or password");
        super.printStackTrace();
    }
}
