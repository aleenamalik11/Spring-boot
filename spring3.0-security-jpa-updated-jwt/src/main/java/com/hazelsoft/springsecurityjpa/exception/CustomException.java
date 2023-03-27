package com.hazelsoft.springsecurityjpa.exception;

public class CustomException extends RuntimeException{

    public CustomException(String s)    {
        // Call constructor of parent Exception
        super(s);
    }
}
