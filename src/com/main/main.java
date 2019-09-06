package com.main;

import com.monitor.GestorDeMonitor;
import com.util.ThreadDistribution;
import com.util.TransitionThread;

public class main {

    public static void main(String[] args){

        GestorDeMonitor monitor = new GestorDeMonitor();
        ThreadDistribution threadDistr = new ThreadDistribution();

        threadDistr.printThreads();

        for(int i=0; i<threadDistr.getNumberOfThreads(); i++){

            TransitionThread transitionThread = new TransitionThread(i, threadDistr.getTransitionsOfThread(i));
            transitionThread.setMonitor(monitor);

            System.out.printf("Run Thread-%d/%d!\n", i, threadDistr.getNumberOfThreads());

            Thread t = new Thread(transitionThread);
            t.start();


        }
    }



}
