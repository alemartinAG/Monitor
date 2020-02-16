package com.monitor;

import java.util.Random;

public class FairPolicy implements Politicas {

    /**
     * Para debugging
     */

    //TODO: inicializar con array de prioridades?

    @Override
    public  int getNext(boolean[] andVector) {

        if(andVector[10]){
            return 10;
        }

        if(andVector[9]){
            return 9;
        }

        if(andVector[11]){
            System.out.println("-----> T12");
            return 11;
        }

        if(andVector[12])
            return 12;

        if(andVector[18] && andVector[21]){
            if(new Random().nextInt(1) > 0.5){
                return 18;
            }
            else{
                return 21;
            }
        }

        for(int i=0; i<andVector.length; i++){

            if(andVector[i]){
                System.out.println("-----> T"+String.valueOf(i+1));
                return i;
            }

        }

        return -1;

    }
}
