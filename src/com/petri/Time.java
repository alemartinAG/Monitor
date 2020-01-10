package com.petri;

import java.util.Date;

public class Time {

    private int alpha, beta;
    private Date timeStamp;
    
    public Time(int alpha, int beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    boolean testTimeWindow(){
        return false;
    }

    void setNewTimeStamp(){
        this.timeStamp = new Date();
    }

    void beforeWindow(){

    }

    void setWaiting(){

    }

    void resetWaiting(){

    }

}


