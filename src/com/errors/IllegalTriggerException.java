package com.errors;

public class IllegalTriggerException extends Exception {

    public final static String defaultMessage = "An illegal trigger has tried to occur";

    public  IllegalTriggerException(){
        super(defaultMessage);
    }

    public  IllegalTriggerException(String message){
        super(message);
    }

}
