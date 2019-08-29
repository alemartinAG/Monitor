package com.util;

import java.util.concurrent.Semaphore;

public class Mutex {

    // Binary Semaphore to implement mutex
    private Semaphore semaphore = new Semaphore(1, true); // Ver Fairness

    public boolean acquire(){

        try {

            semaphore.acquire();

            return true;

        } catch (InterruptedException e) {
            e.printStackTrace();

            return false;
        }

    }

    public void release(){

        semaphore.release();

    }

}
