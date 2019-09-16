package com.main;

import com.monitor.GestorDeMonitor;
import com.monitor.PetriNet;
import com.util.ThreadDistribution;
import com.util.TransitionThread;

import java.util.Arrays;

public class main {

    public static void main(String[] args){

        PetriNet pn = new PetriNet();
        GestorDeMonitor monitor = new GestorDeMonitor(pn, 20);
        ThreadDistribution threadDistr = new ThreadDistribution();

        pn.printMatrix(pn.getMatrix(PetriNet.CIM));

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
