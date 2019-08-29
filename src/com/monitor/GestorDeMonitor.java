package com.monitor;

import com.util.Mutex;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;

    public GestorDeMonitor(){

        mutex = new Mutex();
        petriNet = new PetriNet();

    }

}
