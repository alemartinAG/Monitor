package com.monitor;

import com.errors.IllegalTriggerException;
import com.util.Mutex;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;

    public GestorDeMonitor(){

        mutex = new Mutex();
        petriNet = new PetriNet();

        /*try {
            petriNet.trigger(2);
            petriNet.trigger(0);
            petriNet.trigger(2);
        } catch (IllegalTriggerException e) {
            e.printStackTrace();
        }*/


    }

}
