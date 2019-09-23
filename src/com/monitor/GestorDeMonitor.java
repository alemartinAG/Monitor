package com.monitor;

import com.petri.PetriNet;
import com.util.Log;
import com.util.Mutex;

import java.util.Arrays;

public class GestorDeMonitor {

    private Mutex mutex;
    private PetriNet petriNet;
    private Colas queues;
    private Politicas policy;
    private Log eventLog = new Log();
    private int transitionsLeft;
    private int transitionsTotal;
    public static boolean keeprunning = true;

    /**
     * Clase encargada de implementar la logica del monitor
     * @param net red de petri a utilizar
     * @param limit cantidad de transiciones a disparar
     */
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

                /* Disparo la cantidad de transiciones especificadas */
                transitionsLeft--;
                if(transitionsLeft < 0){
                    keeprunning = false;
                    mutex.release();
                    return;
                }

                System.out.printf("%3d | Transition %d triggered\n", transitionsTotal-transitionsLeft, transition+1);
                String currentMarking = Arrays.toString(petriNet.getMatrix(PetriNet.MRK)[PetriNet.CURRENT]);
                eventLog.log(String.format("T%d%sMarking: %s", transition+1, Log.SEPARATOR, currentMarking));


                boolean[] enabledVector = petriNet.areEnabled().clone();
                boolean[] queueVector = queues.getQueued().clone();
                boolean[] andVector = new boolean[petriNet.getTransitionsCount()];
                Arrays.fill(andVector, false);

                boolean m = false;

                /* Calculo del vector AND */
                for(int i=0; i<petriNet.getTransitionsCount(); i++){
                    if(queueVector[i] && enabledVector[i]){
                        andVector[i] = true;
                        m = true;
                    }
                }

                if(m){
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

    }

}
