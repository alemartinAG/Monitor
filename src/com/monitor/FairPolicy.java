package com.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FairPolicy implements Politicas {

    /**
     * Para debugging
     */

    //TODO: inicializar con array de prioridades?

    @Override
    public  int getNext(boolean[] andVector) {

        ArrayList<Integer> truev = trueVector(andVector);

        int[] p1 = {10,11};
        int[] p2 = {12};
        int[] p3 = {1,2,4,5,7,8};
        int[] p4 = {14,15,16,17,3,6,9};
        int[] p5 = {13,18};
        int[] p6 = {19,20,21,22,23,24};

        ArrayList<int[]> priorities = new ArrayList<>();
        priorities.add(p1);
        priorities.add(p2);
        priorities.add(p3);
        priorities.add(p4);
        priorities.add(p5);
        priorities.add(p6);


        /*System.out.print("****");
        for(Integer tran : truev){
            System.out.printf(" %2d ", tran+1);
        }
        System.out.println("****");*/

        for(int i=0; i<priorities.size(); i++){

            for(int j=0; j<priorities.get(i).length; j++){

                if(andVector[priorities.get(i)[j]-1]){
                    return priorities.get(i)[j]-1;
                }

            }
        }
        /*if(andVector[10]){
            return 10;
        }

        if(andVector[9]){
            return 9;
        }

        if(andVector[0])
            return 0;

        if(andVector[3])
            return 3;

        if(andVector[6])
            return 6;

        if(andVector[11]){
            System.out.println("-----> T12");
            return 11;
        }

        if(andVector[18] && andVector[21]){
            if(new Random().nextInt(1) > 0.5){
                return 18;
            }
            else{
                return 21;
            }
        }*/

        /*if(andVector[19])
            return 19;

        if(andVector[20])
            return 20;

        if(andVector[22])
            return 22;

        if(andVector[23])
            return 23;*/

        /*for(int i=0; i<andVector.length; i++){

            if(andVector[i]){
                System.out.println("-----> T"+String.valueOf(i+1));
                return i;
            }

        }*/

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
