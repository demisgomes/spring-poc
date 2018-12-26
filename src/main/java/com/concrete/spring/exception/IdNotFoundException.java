package com.concrete.spring.exception;

public class IdNotFoundException extends Exception{
    public IdNotFoundException(){
        super("Id does not match with any user");
        super.printStackTrace();
    }
}
