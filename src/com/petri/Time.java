package com.petri;

import java.sql.Timestamp;

public class Time {


    // Alpha y Beta se pasan de segundos a milisegundos
    public final static int RATIO = 100;

    private int alpha, beta;
    private long elapsedTime;
    private Timestamp timeStamp;

    public Time(int alpha, int beta) {
        this.alpha = alpha * RATIO;
        this.beta = beta * RATIO;
    }

    public boolean testTimeWindow() {

        long now = System.currentTimeMillis() - timeStamp.getTime();

        if (now >= alpha && now <= beta) {
            return true;
        }

        return false;
    }

    public void setNewTimeStamp() {
        timeStamp = new Timestamp(System.currentTimeMillis());
    }

    void setElapsedTime(){
        elapsedTime = System.currentTimeMillis() - timeStamp.getTime();
    }

    public long getElapsedTime(){return elapsedTime;}

    boolean beforeWindow() {
        return (System.currentTimeMillis() - timeStamp.getTime()) < alpha;
    }

    public long getSleepTime(){

        long enabledTime = timeStamp.getTime();
        long now = System.currentTimeMillis();

        return (enabledTime + alpha) - now;
    }
    public int getAlpha(){return alpha/RATIO;}
    public int getBeta(){return beta/RATIO;}
    public Timestamp getTimestamp(){return timeStamp;}

}
