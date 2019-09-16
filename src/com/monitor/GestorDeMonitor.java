package com.monitor;

import com.errors.IllegalTriggerException;
import com.util.Mutex;

import java.sql.Timestamp;
import java.util.Arrays;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;
    private Colas queues;
    private Politicas policy;
    private int transitionsLeft;
    private int transitionsTotal;
    public static boolean keeprunning = true;

    public GestorDeMonitor(PetriNet net, int limit){

        mutex = new Mutex();
        petriNet = net;
        policy = new FairPolicy();
        queues = new Colas(petriNet.getTransitionsCount());
        transitionsLeft = limit;
        transitionsTotal = limit;
    }

    public void fireTransition(int transition){

        boolean k = true;
        boolean result = true;

        mutex.acquire();
        //System.out.printf("Acquired by Thread-%s\n", Thread.currentThread().getName());

        while(k){

            result = petriNet.trigger(transition);

            if(result){

                transitionsLeft--;

                if(transitionsLeft < 0){
                    keeprunning = false;
                    mutex.release();
                    return;
                }

                boolean[] enabledVector = petriNet.areEnabled().clone();
                boolean[] queueVector = queues.getQueued().clone();

                boolean[] andVector = new boolean[petriNet.getTransitionsCount()];
                Arrays.fill(andVector, false);

                boolean m = false;

                System.out.printf("%3d | Transition %d triggered\n", transitionsTotal-transitionsLeft, transition+1);

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
