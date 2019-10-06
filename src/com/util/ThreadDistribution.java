package com.util;

import java.util.ArrayList;


public class ThreadDistribution {

    private static final String THREADS = "res/threads.txt";
    private ArrayList<ArrayList<Integer>> threads_transitions;

    public ThreadDistribution(){
        threads_transitions = new Parser(THREADS, "\\d+", "(", ")").getParsedElements();
    }

    public ThreadDistribution(String file){
        threads_transitions = new Parser(file, "\\d+", "(", ")").getParsedElements();
    }

    /* Devuelve el numero de threads necesarios */
    public int getNumberOfThreads(){
        return threads_transitions.size();
    }

    /* Devuelve las transiciones del thread especificado */
    public ArrayList<Integer> getTransitionsOfThread(int thread_number){
        return threads_transitions.get(thread_number);
    }

    /* Funcion utilizada para debugging */
    public void printThreads(){
        for(int i=0; i<threads_transitions.size(); i++){
            System.out.printf("Thread-%02d: ", i);
            for(int j=0; j<threads_transitions.get(i).size(); j++){
                System.out.printf("T%d ", threads_transitions.get(i).get(j));
            }
            System.out.println();
        }
    }
}
