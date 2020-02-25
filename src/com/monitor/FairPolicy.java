package com.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class FairPolicy implements Politicas {

    /**
     * Para debugging
     */

    //TODO: inicializar con array de prioridades?

    @Override
    public  int getNext(boolean[] andVector) {

        ArrayList<Integer> truev = trueVector(andVector);

        int[] p1 = {10,11};
        int[] p2 = {14};
        int[] p3 = {1,2,4,5,7,8};
        int[] p4 = {12};
        int[] p5 = {15,16,17,3,6,9};
        int[] p6 = {13,18};
        int[] p7 = {21,24,22};
        int[] p8 = {20,19,23};

        ArrayList<int[]> priorities = new ArrayList<>();
        priorities.add(p1);
        priorities.add(p2);
        priorities.add(p3);
        priorities.add(p4);
        priorities.add(p5);
        priorities.add(p6);
        priorities.add(p7);
        priorities.add(p8);


        for(int i=0; i<priorities.size(); i++){

            for(int j=0; j<priorities.get(i).length; j++){
                Vector<Integer> enabled = new Vector<>();

                if(andVector[priorities.get(i)[j]-1]){
                    //return priorities.get(i)[j]-1;
                    enabled.add(priorities.get(i)[j]-1);
                }

                if(!enabled.isEmpty()){
                    return enabled.get(new Random().nextInt(enabled.size()));
                }

            }
        }

        int transition =  trueVector(andVector).get(new Random().nextInt(trueVector(andVector).size()));
        System.out.println("-----> T" + (transition + 1));

        return transition;
        //return -1;

    }

    private ArrayList<Integer> trueVector(boolean[] vector){

        ArrayList<Integer> candidates = new ArrayList<Integer>();

        for(int i=0; i<vector.length; i++){
            if(vector[i]){
                candidates.add(i);
            }
        }

        return candidates;
    }
}
