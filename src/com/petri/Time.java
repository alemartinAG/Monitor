package com.petri;

import java.sql.Timestamp;

public class Time {

    private int alpha, beta;
    private boolean beforeWindow, waiting = false;
    private Timestamp timeStamp;

    public Time(int alpha, int beta) {
        this.alpha = alpha * 1000;
        this.beta = beta * 1000;
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

}
