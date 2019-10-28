package com.petri;

import com.monitor.GestorDeMonitor;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TransitionThread implements Runnable {

    private int thread_number;
    private ArrayList<Integer> thread_transitions;
    private GestorDeMonitor monitor;
    private int transitions_number;
    private CyclicBarrier barrier;

    /**
     *
     * @param index número del thread según orden de creación
     * @param transitions transiciones asignadas al thread
     */
    public TransitionThread(int index, ArrayList<Integer> transitions, CyclicBarrier barrier){
        this.barrier = barrier;
        thread_number = index;
        thread_transitions = new ArrayList<>();
        thread_transitions = transitions;
        transitions_number = thread_transitions.size();
    }

    /* Le asigno el monitor */
    public void setMonitor(GestorDeMonitor gestor){
        monitor = gestor;
    }

    @Override
    public void run() {

        // Seteo el nombre del thread de acuerdo al orden de creacion
        Thread.currentThread().setName(String.format("%d", thread_number));

        while(GestorDeMonitor.keeprunning){

            /* El hilo trata de disparar sus transiciones, una por una */
            for(int i=0; i<transitions_number; i++){

                /* Para no acaparar el semáforo */
                try {
                    Thread.sleep(new Random().nextInt(20));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                monitor.fireTransition(thread_transitions.get(i)-1);
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }


    }

}
