package com.monitor;

public class FairPolicy implements Politicas {

    @Override
    public  int getNext(boolean[] andVector) {

        if(andVector[1]){
            return 1;
        }
        else if(andVector[4]){
            return 4;
        }
        else if(andVector[5]){
            return 5;
        }
        else if (andVector[0]){
            return 0;
        }
        else if(andVector[2]){
            return 2;
        }
        else {
            return 3;
        }
    }
}
