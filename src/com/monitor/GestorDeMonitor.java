package com.monitor;

import com.errors.IllegalTriggerException;
import com.util.Mutex;

import java.lang.reflect.Array;
import java.sql.Timestamp;
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
        System.out.printf("Acquired by Thread-%s\n", Thread.currentThread().getName());

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
                //System.out.printf("[%s] Mutex released by Thread-%s and went to SLEEP\n", (new Timestamp(System.currentTimeMillis())),Thread.currentThread().getName());
                mutex.release();
                queues.sleepThread(transition);

            }

        }

        //System.out.printf("[%s] Mutex released by Thread-%s\n", (new Timestamp(System.currentTimeMillis())),Thread.currentThread().getName());
        mutex.release();
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

}
