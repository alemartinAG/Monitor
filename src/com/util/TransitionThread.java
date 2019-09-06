package com.util;

import com.monitor.GestorDeMonitor;

import java.util.ArrayList;

public class TransitionThread implements Runnable {

    private int thread_number;
    private ArrayList<Integer> thread_transitions;
    private GestorDeMonitor monitor;
    private int transitions_number;

    public TransitionThread(int index, ArrayList<Integer> transitions){
        thread_number = index;
        thread_transitions = new ArrayList<>();
        thread_transitions = transitions;
        transitions_number = thread_transitions.size();
    }

    public void setMonitor(GestorDeMonitor gestor){
        monitor = gestor;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(String.format("%d", thread_number));

        while(true){
            for(int i=0; i<transitions_number; i++){
                //System.out.printf("Fire T%d from Thread-%s\n", thread_transitions.get(i), Thread.currentThread().getName());
                monitor.fireTransition(thread_transitions.get(i)-1);
            }
        }

    }

}
