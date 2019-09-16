package com.monitor;

public class FairPolicy implements Politicas {

    /**
     * Para debugging
     */

    @Override
    public  int getNext(boolean[] andVector) {

        for(int i=0; i<andVector.length; i++){
            if(andVector[i])
                return i;
        }

        return -1;

    }
}
