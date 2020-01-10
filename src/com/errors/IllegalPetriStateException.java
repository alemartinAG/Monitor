package com.errors;

public class IllegalPetriStateException extends Exception {

    private final static String defaultMessage = "An illegal state has been reached";

    public IllegalPetriStateException(){
        super(defaultMessage);
    }

    public IllegalPetriStateException(String message){
        super(String.format("%s :: %s", defaultMessage, message));
    }

}
