package com.monitor;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Colas {

    private int nQueues;
    private ArrayList<Semaphore> semaphoreList;

    public Colas(int threads){
        semaphoreList = new ArrayList<>();
        nQueues = threads;

        for(int i=0; i<nQueues; i++){
            semaphoreList.add(new Semaphore(0));
        }

    }

    public void sleepThread(int thread_number){
        try {
            semaphoreList.get(thread_number).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void wakeThread(int thread_number){
        semaphoreList.get(thread_number).release();
    }

    public boolean[] getQueued(){
        boolean[] inQueue = new boolean[nQueues];

        for(int i=0; i<nQueues; i++){
            if(semaphoreList.get(i).hasQueuedThreads()){
                inQueue[i] = true;
            }
            else {
                inQueue[i] = false;
            }
        }
        return inQueue;
    }
}
