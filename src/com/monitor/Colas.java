package com.monitor;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Colas {

    private int nQueues;
    private ArrayList<Semaphore> semaphoreList;

    /**
     * Creo una cola para cada transicion, se utiliza un
     * semáforo inicializado en 0 para cada cola
     *
     * @param transitions cantidad de colas para la red
     */
    Colas(int transitions){
        semaphoreList = new ArrayList<>();
        nQueues = transitions;

        for(int i=0; i<nQueues; i++){
            semaphoreList.add(new Semaphore(0));
        }

    }

    /* Se encarga de dormir al thread en su correspondiente semaforo */
    void sleepThread(int thread_number){
        try {
            semaphoreList.get(thread_number).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Se encarga de liberar el thread de su semaforo */
    void wakeThread(int thread_number){
        semaphoreList.get(thread_number).release();
    }

    /* Devuelve un vector con los semaforos ocupados */
    boolean[] getQueued(){
        boolean[] inQueue = new boolean[nQueues];

        for(int i=0; i<nQueues; i++){
            inQueue[i] = semaphoreList.get(i).hasQueuedThreads();
        }
        return inQueue;
    }
}