package com.petri;

import java.sql.Timestamp;

public class Time {

    // Alpha y Beta se multiplican por mil para obtener segundos!
    private int alpha, beta;
    private boolean beforeWindow, waiting = false;
    private Timestamp timeStamp;

    public Time(int alpha, int beta) {
        this.alpha = alpha * 1000;
        this.beta = beta * 1000;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    public boolean testTimeWindow() {

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

    public void setNewTimeStamp() {
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    public boolean beforeWindow() {
        return beforeWindow;
    }

    public void setWaiting() {
        waiting = true;
    }

    public boolean isWaiting(){
        return waiting;
    }

    public long getSleepTime(){
        return timeStamp.getTime() + alpha - System.currentTimeMillis();
    }

    public void resetWaiting() {

    }

    public Timestamp getTimestamp () {
        return timeStamp;
    }

}
