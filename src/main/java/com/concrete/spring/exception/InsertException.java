package com.concrete.spring.exception;

public class InsertException extends Exception {
    public InsertException(){
        super("Failed to insert into database");
        super.printStackTrace();
    }

    public InsertException(String message){
        super(message);
        super.printStackTrace();
    }

}
