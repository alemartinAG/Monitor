package com.util;

import java.util.concurrent.Semaphore;

public class Mutex {

    /**
     * Uso un semáforo binario para implementar un mutex
     */

    private Semaphore semaphore;

    public Mutex(){
        semaphore = new Semaphore(1, true);
    }

    public void acquire(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release(){
        semaphore.release();
    }

}
