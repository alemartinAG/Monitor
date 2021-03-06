package com.main;

import com.errors.IllegalPetriStateException;
import com.monitor.GestorDeMonitor;
import com.petri.PetriNet;
import com.petri.TInvariant;
import com.util.ThreadDistribution;
import com.petri.TransitionThread;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class main {

    public static void main(String[] args) {

        PetriNet pn = new PetriNet(true);
        GestorDeMonitor monitor = new GestorDeMonitor(pn, 2500);
        ThreadDistribution threadDistr = new ThreadDistribution();

        System.out.println("Combined Incidence Matrix");
        pn.printMatrix(pn.getMatrix(PetriNet.CIM));
        System.out.println("Inhibition Matrix");
        pn.printMatrix(pn.getMatrix(PetriNet.INM));

        threadDistr.printThreads();

        int n_threads = threadDistr.getNumberOfThreads();

        CyclicBarrier barrier = new CyclicBarrier(n_threads + 1);

        for (int i = 0; i < n_threads; i++) {

            TransitionThread transitionThread = new TransitionThread(i, threadDistr.getTransitionsOfThread(i), monitor, barrier);

            System.out.printf("Run Thread-%d/%d!\n", i + 1, threadDistr.getNumberOfThreads());

            Thread t = new Thread(transitionThread);
            t.start();

        }

        try {
            barrier.await();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("\nFin de ejecución...\n");

        /*TInvariant tInvariant = new TInvariant();
        try {
            tInvariant.checkInvariants(null);
        } catch (IllegalPetriStateException e) {
            e.printStackTrace();
        }*/
        //tInvariant.checkcheck();
        //System.out.print(tInvariant.check());
    }

}
