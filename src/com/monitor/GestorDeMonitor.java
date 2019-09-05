package com.monitor;

import com.errors.IllegalTriggerException;
import com.util.Mutex;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;
    private Colas queues;

    public GestorDeMonitor(){

        mutex = new Mutex();
        petriNet = new PetriNet();
        queues = new Colas(petriNet.getTransitionsCount());

    }

    public void fireTransition(int transition){

        boolean k = true;
        boolean result = true;

        mutex.acquire();

        while(k){

            result = petriNet.trigger(transition);

            if(result){

                boolean[] enabledVector = petriNet.areEnabled();
                boolean[] queueVector = queues.getQueued();
                int m = -1;

                for(int i=0; i<petriNet.getTransitionsCount(); i++){
                    if(queueVector[i] && enabledVector[i]){
                        m = i;
                        break;
                    }
                }

                if(m >= 0){
                    //TODO: despertar hilo segun política?
                    queues.wakeThread(m);
                    return;
                }
                else {
                    k = false;
                }


            }
            else {
                mutex.release();
                queues.sleepThread(Integer.parseInt(Thread.currentThread().getName()));
            }

        }

        mutex.release();

    }

}
