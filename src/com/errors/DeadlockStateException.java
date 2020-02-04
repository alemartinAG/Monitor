package com.errors;

public class DeadlockStateException extends Exception {

    private final static String defaultMessage = "A state of Deadlock has been reached";

    public DeadlockStateException(){
        super(defaultMessage);
    }

    public DeadlockStateException(String message){
        super(String.format("%s :: %s", defaultMessage, message));
    }

}
