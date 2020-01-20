package com.petri;

import java.sql.Timestamp;

public class Time {

    private final static int RATIO = 1000;
    private int alpha, beta;
    private boolean beforeWindow, waiting = false;
    private Timestamp timeStamp;

    public Time(int alpha, int beta) {
        this.alpha = alpha * RATIO;
        this.beta = beta * RATIO;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    boolean testTimeWindow() {

        long now = System.currentTimeMillis() - timeStamp.getTime();

        if (now >= alpha && now <= beta) {
            return true;
        } else {
            // Compruebo si estoy antes de la ventana
            if (now < alpha) {
                beforeWindow = true;
            } else {
                beforeWindow = false;
            }
        }

        return false;
    }

    void setNewTimeStamp() {
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    boolean beforeWindow() {
        return beforeWindow;
    }

    void setWaiting() {
        waiting = true;
    }

    boolean isWaiting(){
        return waiting;
    }

    long getSleepTime(){
        return timeStamp.getTime() + alpha*1000 - System.currentTimeMillis();
    }

    void resetWaiting() {

    }

    public int getAlpha(){return alpha/RATIO;}
    public int getBeta(){return beta/RATIO;}

}
