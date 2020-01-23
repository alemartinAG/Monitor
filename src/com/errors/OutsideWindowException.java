package com.errors;

public class OutsideWindowException extends Exception{

    private final static String defaultMessage = "The timed transition is enabled but outside it's time window";

    public OutsideWindowException(){
        super(defaultMessage);
    }

}
