package com.monitor;

import com.errors.IllegalTriggerException;
import com.util.Mutex;

import java.lang.reflect.Array;
import java.util.Arrays;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;
    private Colas queues;
    private Politicas policy;

    public GestorDeMonitor(){

        mutex = new Mutex();
        petriNet = new PetriNet();
        policy = new FairPolicy();
        queues = new Colas(petriNet.getTransitionsCount());

    }

    public void fireTransition(int transition){

        boolean k = true;
        boolean result = true;

        mutex.acquire();

        while(k){

            //petriNet.printEnabled();
            //petriNet.printCurrentMarking();

            result = petriNet.trigger(transition);

            if(result){


                boolean[] enabledVector = petriNet.areEnabled().clone();
                boolean[] queueVector = queues.getQueued().clone();

                boolean[] andVector = new boolean[petriNet.getTransitionsCount()];
                Arrays.fill(andVector, false);

                boolean m = false;

                System.out.printf("Transition %d triggered\n", transition+1);

                for(int i=0; i<petriNet.getTransitionsCount(); i++){
                    //System.out.printf("i = %d // qV = %d // eV = %d\n", i, queueVector.length, enabledVector.length);

                    if(queueVector[i] && enabledVector[i]){
                        //System.out.printf("m=%d // break\n", i);
                        andVector[i] = true;
                        m = true;
                    }
                }

                if(m){
                    //TODO: despertar hilo segun política?
                    //System.out.println("m>=0 // Despierto");
                    queues.wakeThread(policy.getNext(andVector));
                    return;
                }
                else {
                    //System.out.println("m<0 // k=false");
                    k = false;
                }


            }
            else {
                //System.out.printf("THREAD %s WENT TO SLEEP\n", Thread.currentThread().getName());
                mutex.release();
                //queues.sleepThread(Integer.parseInt(Thread.currentThread().getName()));
                queues.sleepThread(transition);

            }

        }

        System.out.printf("Mutex released by Thread-%s\n", Thread.currentThread().getName());
        mutex.release();

    }

}
