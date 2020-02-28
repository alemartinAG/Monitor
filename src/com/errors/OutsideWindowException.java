package com.errors;

public class OutsideWindowException extends Exception{

    private final static String defaultMessage = "The timed transition is enabled but outside it's time window";
    private boolean before;
    private long sleepTime;

    public OutsideWindowException(boolean before, long sleepTime){
        super(defaultMessage);
        this.before = before;
        this.sleepTime = sleepTime;
    }

    public boolean isBefore(){
        return before;
    }

    public long timeToSleep(){
        return sleepTime;
    }

}
