package com.main;

import com.monitor.GestorDeMonitor;
import com.petri.PInvariant;
import com.petri.PetriNet;
import com.petri.TInvariant;
import com.util.ThreadDistribution;
import com.petri.TransitionThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class main {

    public static void main(String[] args){

        PetriNet pn = new PetriNet();
        GestorDeMonitor monitor = new GestorDeMonitor(pn, 200);
        ThreadDistribution threadDistr = new ThreadDistribution();

        pn.printMatrix(pn.getMatrix(PetriNet.CIM));

        threadDistr.printThreads();

        int n_threads = threadDistr.getNumberOfThreads();

        CyclicBarrier barrier = new CyclicBarrier(n_threads+1);

        for(int i=0; i<n_threads; i++){

            TransitionThread transitionThread = new TransitionThread(i, threadDistr.getTransitionsOfThread(i), barrier);
            transitionThread.setMonitor(monitor);

            System.out.printf("Run Thread-%d/%d!\n", i+1, threadDistr.getNumberOfThreads());

            Thread t = new Thread(transitionThread);
            t.start();

        }

        try {
            System.out.println("Espera el main ....");
            barrier.await();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("Continua el main ....");


        PInvariant pinv = new PInvariant();
        System.out.println("P INVARIANTES: "+pinv.checkInvariants(pn.getInitalMarking()));

        TInvariant tinv = new TInvariant();
        System.out.println("T INVARIANTES: "+tinv.checkInvariants(pn.getInitalMarking()));
    }

}
